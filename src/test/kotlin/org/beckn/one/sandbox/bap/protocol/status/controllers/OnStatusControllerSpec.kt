package org.beckn.one.sandbox.bap.protocol.status.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.OnOrderStatusDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOrderFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.protocol.schemas.*
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
internal class OnStatusControllerSpec @Autowired constructor(
  private val mockMvc: MockMvc,
  private val mapper: ObjectMapper,
  private val onStatusResponseRepo: BecknResponseRepository<OnOrderStatusDao>,
) : DescribeSpec() {
  private val postOnStatusUrl = "/protocol/v1/on_status"
  val onStatusResponse = ProtocolOnOrderStatus(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnOrderStatusMessage(order = ProtocolOrderFactory.create(1))
  )

  init {

    describe("Protocol OnStatus API") {

      context("when posted to with a valid response") {
        onStatusResponseRepo.clear()
        val postOnStatusResponse = mockMvc
          .perform(
            post(postOnStatusUrl)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsBytes(onStatusResponse))
          )

        it("should respond with status as 200") {
          postOnStatusResponse.andExpect(status().isOk)
        }

        it("should save on status response in db") {
          onStatusResponseRepo.findByMessageId(onStatusResponse.context!!.messageId).size shouldBeExactly 1
        }
      }

      context("when error occurs when processing request") {
        val mockService = mock<ResponseStorageService<ProtocolOnOrderStatus>> {
          onGeneric { save(onStatusResponse) }.thenReturn(Either.Left(DatabaseError.OnWrite))
        }
        val controller = OnStatusController(mockService)

        it("should respond with internal server error") {
          val response = controller.onStatus(onStatusResponse)
          response.statusCode shouldBe DatabaseError.OnWrite.status()
        }
      }
    }
  }

}