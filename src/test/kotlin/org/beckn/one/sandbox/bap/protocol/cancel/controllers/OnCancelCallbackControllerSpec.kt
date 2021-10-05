package org.beckn.one.sandbox.bap.protocol.cancel.controllers

import arrow.core.Either
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.OnCancelDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOrderFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.protocol.schemas.ProtocolOnCancel
import org.beckn.protocol.schemas.ProtocolOnCancelMessage
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.fasterxml.jackson.databind.ObjectMapper

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
internal class OnCancelCallbackControllerSpec @Autowired constructor(
  @Autowired
  private val mockMvc: MockMvc,
  @Autowired
  private val mapper: ObjectMapper,
  @Autowired
  private val onCancelResponseRepo: BecknResponseRepository<OnCancelDao>
) : DescribeSpec() {
  private val postOnCancelUrl = "/protocol/v1/on_cancel"

  val onCancelResponse = ProtocolOnCancel(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnCancelMessage(
      order = ProtocolOrderFactory.create(1)
    )
  )

  init {

    describe("Protocol OnCancel API") {

      context("when posted to with a valid response") {
        onCancelResponseRepo.clear()
        val postOnCancelResponse = mockMvc
          .perform(
            MockMvcRequestBuilders.post(postOnCancelUrl)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsBytes(onCancelResponse))
          )

        it("should respond with status as 200") {
          postOnCancelResponse.andExpect(MockMvcResultMatchers.status().isOk)
        }

        it("should save on cancel response in db") {
          onCancelResponseRepo.findByMessageId(onCancelResponse.context!!.messageId).size shouldBeExactly 1
        }
      }

      context("when error occurs when processing request") {
        val mockService = mock<ResponseStorageService<ProtocolOnCancel>> {
          onGeneric { save(onCancelResponse) }.thenReturn(Either.Left(DatabaseError.OnWrite))
        }
        val controller = OnCancelCallbackController(mockService)

        it("should respond with internal server error") {
          val response = controller.onCancel(onCancelResponse)
          response.statusCode shouldBe DatabaseError.OnWrite.status()
        }
      }
    }
  }

}