package org.beckn.one.sandbox.bap.protocol.confirm.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.OnConfirmDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOrderFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.protocol.schemas.ProtocolOnConfirm
import org.beckn.protocol.schemas.ProtocolOnConfirmMessage
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
internal class OnConfirmCallbackControllerSpec @Autowired constructor(
  @Autowired
  private val mockMvc: MockMvc,
  @Autowired
  private val mapper: ObjectMapper,
  @Autowired
  private val onConfirmResponseRepo: BecknResponseRepository<OnConfirmDao>,
) : DescribeSpec() {
  private val postOnConfirmUrl = "/protocol/v1/on_confirm"

  val onConfirmResponse = ProtocolOnConfirm(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnConfirmMessage(
      order = ProtocolOrderFactory.create(1)
    )
  )

  init {

    describe("Protocol OnConfirm API") {

      context("when posted to with a valid response") {
        onConfirmResponseRepo.clear()
        val postOnConfirmResponse = mockMvc
          .perform(
            post(postOnConfirmUrl)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsBytes(onConfirmResponse))
          )

        it("should respond with status as 200") {
          postOnConfirmResponse.andExpect(status().isOk)
        }

        it("should save on confirm response in db") {
          onConfirmResponseRepo.findByMessageId(onConfirmResponse.context!!.messageId).size shouldBeExactly 1
        }
      }

      context("when error occurs when processing request") {
        val mockService = mock<ResponseStorageService<ProtocolOnConfirm>> {
          onGeneric { save(onConfirmResponse) }.thenReturn(Either.Left(DatabaseError.OnWrite))
        }
        val controller = OnConfirmCallbackController(mockService)

        it("should respond with internal server error") {
          val response = controller.onConfirm(onConfirmResponse)
          response.statusCode shouldBe DatabaseError.OnWrite.status()
        }
      }
    }
  }

}