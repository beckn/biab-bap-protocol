package org.beckn.one.sandbox.bap.protocol.shared.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.common.factories.MockNetwork
import org.beckn.one.sandbox.bap.errors.registry.RegistryLookupError
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
class RegistryServiceRetrySpec @Autowired constructor(
  val registryService: RegistryService,
  val objectMapper: ObjectMapper,
) : DescribeSpec() {

  init {
    describe("Lookup Gateways") {
      MockNetwork.startAllSubscribers()
      val allGateways = MockNetwork.getAllGateways()

      beforeEach {
        MockNetwork.resetAllSubscribers()
      }

      it("should retry lookup gateway call if api returns error") {
        stubRegistryLookupApi(response = serverError())
        stubRegistryLookupApi(forState = "Success", response = okJson(objectMapper.writeValueAsString(allGateways)))

        val response = registryService.lookupGateways()

        response
          .fold(
            { Assertions.fail("Lookup should have been retried but it wasn't. Response: $it") },
            { it shouldBe allGateways }
          )
        MockNetwork.registry.verify(2, postRequestedFor(urlEqualTo("/lookup")))
      }

      it("should fail after max retry attempts") {
        stubRegistryLookupApi(response = serverError(), forState = STARTED, nextState = "Failure")
        stubRegistryLookupApi(response = serverError(), forState = "Failure", nextState = "Failure")

        val response = registryService.lookupGateways()

        response shouldBeLeft RegistryLookupError.Internal
        verifyRegistryLookupApiIsInvoked(3)
      }
    }

    describe("Lookup BPP by ID") {
      MockNetwork.startAllSubscribers()
      val bpp = MockNetwork.getRetailBengaluruBpp()
      val bppLookupResponse = listOf(bpp)

      beforeEach {
        MockNetwork.resetAllSubscribers()
      }

      it("should retry lookup bpp by id call if api returns error") {
        stubRegistryLookupApi(response = serverError(), registry = MockNetwork.registryBppLookupApi)
        stubRegistryLookupApi(
          forState = "Success",
          response = okJson(objectMapper.writeValueAsString(bppLookupResponse)),
          registry = MockNetwork.registryBppLookupApi
        )

        val response = registryService.lookupBppById(bpp.subscriber_id)

        response
          .fold(
            { Assertions.fail("Lookup should have been retried but it wasn't. Response: $it") },
            { it shouldBe bppLookupResponse }
          )
        verifyRegistryLookupApiIsInvoked(2, registry = MockNetwork.registryBppLookupApi)
      }

      it("should fail after max retry attempts") {
        stubRegistryLookupApi(
          response = serverError(),
          forState = STARTED,
          nextState = "Failure",
          registry = MockNetwork.registryBppLookupApi
        )
        stubRegistryLookupApi(
          response = serverError(),
          forState = "Failure",
          nextState = "Failure",
          registry = MockNetwork.registryBppLookupApi
        )

        val response = registryService.lookupBppById(bpp.subscriber_id)

        response shouldBeLeft RegistryLookupError.Internal
        verifyRegistryLookupApiIsInvoked(3, registry = MockNetwork.registryBppLookupApi)
      }
    }
  }

  private fun verifyRegistryLookupApiIsInvoked(
    numberOfTimes: Int = 1,
    registry: WireMockServer = MockNetwork.registry,
  ) {
    registry.verify(numberOfTimes, postRequestedFor(urlEqualTo("/lookup")))
  }

  private fun stubRegistryLookupApi(
    forState: String = STARTED,
    nextState: String = "Success",
    response: ResponseDefinitionBuilder?,
    registry: WireMockServer = MockNetwork.registry,
  ) {
    registry
      .stubFor(
        post("/lookup")
          .inScenario("Retry Scenario")
          .whenScenarioStateIs(forState)
          .willReturn(response)
          .willSetStateTo(nextState)
      )
  }
}
