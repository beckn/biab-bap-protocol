package org.beckn.one.sandbox.bap.protocol.cancellation.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.OnCancellationReasonDao
import org.beckn.one.sandbox.bap.message.entities.OnRatingDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.one.sandbox.bap.protocol.cancellation.controller.OnCancellationReasonCallbackController
import org.beckn.one.sandbox.bap.protocol.rating.controllers.OnRatingController
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
internal class OnCancellationReasonCallbackControllerSpec @Autowired constructor(
  @Autowired
  private val mockMvc: MockMvc,
  @Autowired
  private val mapper: ObjectMapper,
  @Autowired
  private val OoCancellationReasonDao: BecknResponseRepository<OnCancellationReasonDao>,
) : DescribeSpec() {
  private val postOnRatingUrl = "/protocol/v1/cancellation_reasons"
  val onCancellationReasonsResponse = ProtocolOnCancellationReasons(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnCancellationReasonMessage(
      cancellationReasons = listOf(ProtocolOption(
        id = "item id 1",
        descriptor = ProtocolDescriptor(name ="item id 1 descriptor")
      ))
    )
  )

  init {

    describe("Protocol On Cancellation Reasons API") {

      context("when posted to with a valid response") {
        OoCancellationReasonDao.clear()
        val postOnRatingResponse = mockMvc
          .perform(
            MockMvcRequestBuilders.post(postOnRatingUrl)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsBytes(onCancellationReasonsResponse))
          )

        it("should respond with status as 200") {
          postOnRatingResponse.andExpect(MockMvcResultMatchers.status().isOk)
        }

        it("should save on rating response in db") {
          OoCancellationReasonDao.findByMessageId(onCancellationReasonsResponse.context!!.messageId).size shouldBeExactly 1
        }
      }

      context("when error occurs when processing request") {
        val mockService = mock<ResponseStorageService<ProtocolOnCancellationReasons>> {
          onGeneric { save(onCancellationReasonsResponse) }.thenReturn(Either.Left(DatabaseError.OnWrite))
        }
        val controller = OnCancellationReasonCallbackController(mockService)

        it("should respond with internal server error") {
          val response = controller.onCancellationReasons(onCancellationReasonsResponse)
          response.statusCode shouldBe DatabaseError.OnWrite.status()
        }
      }
    }
  }

}