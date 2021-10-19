package org.beckn.one.sandbox.bap.protocol.rating.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.OnRatingDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
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
internal class OnRatingControllerSpec @Autowired constructor(
  @Autowired
  private val mockMvc: MockMvc,
  @Autowired
  private val mapper: ObjectMapper,
  @Autowired
  private val onRatingResponseRepo: BecknResponseRepository<OnRatingDao>,
) : DescribeSpec() {
  private val postOnRatingUrl = "/protocol/v1/on_rating"
  val onRatingResponse = ProtocolOnRating(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnRatingMessage(
      feedback = ProtocolOnRatingMessageFeedback(
        id = "item id 1", descriptor = "item id 1 descriptor", parentId = "item id 1 parent item id 1"
      )
    )
  )

  init {

    describe("Protocol OnRating API") {

      context("when posted to with a valid response") {
        onRatingResponseRepo.clear()
        val postOnRatingResponse = mockMvc
          .perform(
            post(postOnRatingUrl)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsBytes(onRatingResponse))
          )

        it("should respond with status as 200") {
          postOnRatingResponse.andExpect(status().isOk)
        }

        it("should save on rating response in db") {
          onRatingResponseRepo.findByMessageId(onRatingResponse.context!!.messageId).size shouldBeExactly 1
        }
      }

      context("when error occurs when processing request") {
        val mockService = mock<ResponseStorageService<ProtocolOnRating>> {
          onGeneric { save(onRatingResponse) }.thenReturn(Either.Left(DatabaseError.OnWrite))
        }
        val controller = OnRatingController(mockService)

        it("should respond with internal server error") {
          val response = controller.onRating(onRatingResponse)
          response.statusCode shouldBe DatabaseError.OnWrite.status()
        }
      }
    }
  }

}