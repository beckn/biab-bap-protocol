package org.beckn.one.sandbox.bap.protocol.shared.services

import arrow.core.Either
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.common.City
import org.beckn.one.sandbox.bap.common.Country
import org.beckn.one.sandbox.bap.common.Domain
import org.beckn.one.sandbox.bap.common.factories.SubscriberDtoFactory
import org.beckn.one.sandbox.bap.errors.registry.RegistryLookupError
import org.beckn.one.sandbox.bap.protocol.external.domains.Subscriber
import org.beckn.one.sandbox.bap.protocol.external.registry.RegistryClient
import org.beckn.one.sandbox.bap.protocol.external.registry.SubscriberDto
import org.beckn.one.sandbox.bap.protocol.external.registry.SubscriberLookupRequest
import org.junit.jupiter.api.Assertions.fail
import org.mockito.Mockito.*
import retrofit2.mock.Calls
import java.io.IOException
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal class RegistryServiceSpec : DescribeSpec() {
  init {
    val registryServiceClient = mock(RegistryClient::class.java)
    val bppRegistryServiceClient = mock(RegistryClient::class.java)
    val registryService =
      RegistryService(
        registryServiceClient,
        bppRegistryServiceClient,
        Domain.LocalRetail.value,
        City.Bengaluru.value,
        Country.India.value
      )
    val clock = Clock.fixed(Instant.now(), ZoneId.of("UTC"))
    val request = SubscriberLookupRequest(
      type = Subscriber.Type.BG,
      domain = Domain.LocalRetail.value,
      city = City.Bengaluru.value,
      country = Country.India.value
    )

    describe("Lookup") {
      it("should return subscribers returned by registry") {
        `when`(registryServiceClient.lookup(request)).thenReturn(
          Calls.response(listOf(SubscriberDtoFactory.getDefault(clock = clock)))
        )

        val response: Either<RegistryLookupError, List<SubscriberDto>> = registryService.lookupGateways()

        response
          .fold(
            { fail("Lookup failed. Code: $it.code(), Error: ${it.error()}") },
            { subscribers -> subscribers shouldBe listOf(SubscriberDtoFactory.getDefault(clock = clock)) }
          )
        verify(registryServiceClient).lookup(request)
      }

      it("should return registry error when registry call fails with an IO exception") {
        `when`(registryServiceClient.lookup(request)).thenReturn(
          Calls.failure(IOException("Timeout"))
        )

        val response: Either<RegistryLookupError, List<SubscriberDto>> = registryService.lookupGateways()

        response
          .fold(
            { it shouldBe RegistryLookupError.Internal },
            { fail("Lookup should have timed out but didn't. Response: $it") }
          )
      }

      it("should return registry error when registry call fails with a runtime exception") {
        `when`(registryServiceClient.lookup(request)).thenReturn(
          Calls.failure(RuntimeException("Network error"))
        )

        val response: Either<RegistryLookupError, List<SubscriberDto>> = registryService.lookupGateways()

        response
          .fold(
            { it shouldBe RegistryLookupError.Internal },
            { fail("Lookup should have timed out but didn't. Response: $it") }
          )
      }

      it("should return no gateways found error when registry response is null") {
        `when`(registryServiceClient.lookup(request)).thenReturn(
          Calls.response(null)
        )

        val response: Either<RegistryLookupError, List<SubscriberDto>> = registryService.lookupGateways()

        response
          .fold(
            { it shouldBe RegistryLookupError.NoSubscriberFound },
            { fail("Lookup should have timed out but didn't. Response: $it") }
          )
      }

      it("should return no gateways found error when registry response is empty") {
        `when`(registryServiceClient.lookup(request)).thenReturn(
          Calls.response(emptyList())
        )

        val response: Either<RegistryLookupError, List<SubscriberDto>> = registryService.lookupGateways()

        response
          .fold(
            { it shouldBe RegistryLookupError.NoSubscriberFound },
            { fail("Lookup should have timed out but didn't. Response: $it") }
          )
      }
    }
  }
}