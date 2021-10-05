package org.beckn.one.sandbox.bap.protocol.init.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.*
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.repositories.GenericRepository
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolOnInit
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
internal class PollOnInitResponseControllerSpec @Autowired constructor(
  private val initResponseRepo: BecknResponseRepository<OnInitDao>,
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
    action = ContextDao.Action.INIT,
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
    describe("PollOnInitResponseController") {
      initResponseRepo.clear()
      messageRepository.insertOne(MessageDao(id = entityContext.messageId, type = MessageDao.Type.Init))
      initResponseRepo.insertMany(onInitResponse())

      context("when called for given message id") {
        val onInitCall = mockMvc
          .perform(
            MockMvcRequestBuilders.get("/protocol/response/v1/on_init")
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .param("messageId", entityContext.messageId)
          )

        it("should respond with status ok") {
          onInitCall.andExpect(status().isOk)
        }

        it("should respond with all init responses in body") {
          val results = onInitCall.andReturn()
          val body = results.response.contentAsString
          val response: List<ProtocolOnInit> = mapper.readValue(body)
          response.size shouldBeExactly 2
          response.forEach { it.context!!.bppId shouldBe entityContext.bppId }
        }
      }

      context("when failure occurs during request processing") {
        val mockOnPollService = mock<PollForResponseService<ProtocolOnInit>> {
          onGeneric { findResponses(any()) }.thenReturn(Either.Left(DatabaseError.OnRead))
        }
        val pollInitResponseController = PollInitResponseController(mockOnPollService, contextFactory)
        it("should respond with failure") {
          val response = pollInitResponseController.findResponses(entityContext.messageId)
          response.statusCode shouldBe DatabaseError.OnRead.status()
        }
      }
    }
  }

  fun onInitResponse(): List<OnInitDao> {
    val entityInitResponse = OnInitDao(
      context = entityContext,
      message = OnInitMessageDao(OnInitMessageInitializedDao())
    )
    return listOf(
      entityInitResponse,
      entityInitResponse,
      entityInitResponse.copy(context = entityContext.copy(messageId = "123"))
    )
  }
}