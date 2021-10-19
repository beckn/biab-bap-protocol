package org.beckn.one.sandbox.bap.protocol.rating.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.ContextDao
import org.beckn.one.sandbox.bap.message.entities.MessageDao
import org.beckn.one.sandbox.bap.message.entities.OnRatingDao
import org.beckn.one.sandbox.bap.message.entities.OnRatingMessageDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolOnRatingMessageFeedbackFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.repositories.GenericRepository
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolOnRating
import org.mockito.kotlin.any
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
internal class PollOnRatingResponseControllerSpec @Autowired constructor(
  private val ratingResponseRepo: BecknResponseRepository<OnRatingDao>,
  private val messageRepository: GenericRepository<MessageDao>,
  private val contextFactory: ContextFactory,
  private val mapper: ObjectMapper,
  private val mockMvc: MockMvc
) : DescribeSpec() {

  private val fixedClock = Clock.fixed(
    Instant.parse("2018-11-30T18:35:24.00Z"),
    ZoneId.of("UTC")
  )
  private val entityContext = ContextDao(
    domain = "LocalRetail",
    country = "IN",
    action = ContextDao.Action.RATING,
    city = "Pune",
    coreVersion = "0.9.1-draft03",
    bapId = "http://host.bap.com",
    bapUri = "http://host.bap.com/v1",
    bppId = "http://host.bpp.com",
    bppUri = "http://host.bpp.com/v1",
    transactionId = "222",
    messageId = "222",
    timestamp = OffsetDateTime.now(fixedClock)
  )


  init {
    describe("PollOnTrackResponseController") {
      ratingResponseRepo.clear()
      messageRepository.insertOne(MessageDao(id = entityContext.messageId, type = MessageDao.Type.Track))
      ratingResponseRepo.insertMany(onRatingResponse())

      context("when called for given message id") {
        val onRatingCall = mockMvc
          .perform(
            MockMvcRequestBuilders.get("/protocol/response/v1/on_rating")
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .param("messageId", entityContext.messageId)
          )

        it("should respond with status ok") {
          onRatingCall.andExpect(status().isOk)
        }

        it("should respond with all rating responses in body") {
          val results = onRatingCall.andReturn()
          val body = results.response.contentAsString
          val response: List<ProtocolOnRating> = mapper.readValue(body)
          response.size shouldBeExactly 2
          response.forEach { it.context!!.bppId shouldBe entityContext.bppId }
        }
      }

      context("when failure occurs during request processing") {
        val mockOnPollService = mock<PollForResponseService<ProtocolOnRating>> {
          onGeneric { findResponses(any()) }.thenReturn(Either.Left(DatabaseError.OnRead))
        }
        val pollRatingResponseController = PollRatingResponseController(mockOnPollService, contextFactory)
        it("should respond with failure") {
          val response = pollRatingResponseController.findResponses(entityContext.messageId)
          response.statusCode shouldBe DatabaseError.OnRead.status()
        }
      }
    }
  }

  fun onRatingResponse(): List<OnRatingDao> {
    val entityRatingResponse = OnRatingDao(
      context = entityContext,
      message = OnRatingMessageDao(
        feedback = ProtocolOnRatingMessageFeedbackFactory.createAsEntity(
          ProtocolOnRatingMessageFeedbackFactory.create(1)
        )
      )
    )
    return listOf(
      entityRatingResponse,
      entityRatingResponse,
      entityRatingResponse.copy(context = entityContext.copy(messageId = "123"))
    )
  }
}