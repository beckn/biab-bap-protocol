package org.beckn.one.sandbox.bap.protocol.init.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.OnInitDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOnInitMessageInitializedFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.protocol.schemas.ProtocolOnInit
import org.beckn.protocol.schemas.ProtocolOnInitMessage
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
internal class OnInitCallbackControllerSpec @Autowired constructor(
  @Autowired
  private val mockMvc: MockMvc,
  @Autowired
  private val mapper: ObjectMapper,
  @Autowired
  private val onInitResponseRepo: BecknResponseRepository<OnInitDao>,
) : DescribeSpec() {
  private val postOnInitUrl = "/protocol/v1/on_init"

  val onInitResponse = ProtocolOnInit(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnInitMessage(
      order = ProtocolOnInitMessageInitializedFactory.create(1, 2)
    )
  )

  init {

    describe("Protocol OnSelect API") {

      context("when posted to with a valid response") {
        onInitResponseRepo.clear()
        val postOnInitResponse = mockMvc
          .perform(
            post(postOnInitUrl)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsBytes(onInitResponse))
          )

        it("should respond with status as 200") {
          postOnInitResponse.andExpect(status().isOk)
        }

        it("should save on init response in db") {
          onInitResponseRepo.findByMessageId(onInitResponse.context!!.messageId).size shouldBeExactly 1
        }
      }

      context("when error occurs when processing request") {
        val mockService = mock<ResponseStorageService<ProtocolOnInit>> {
          onGeneric { save(onInitResponse) }.thenReturn(Either.Left(DatabaseError.OnWrite))
        }
        val controller = OnInitCallbackController(mockService)

        it("should respond with internal server error") {
          val response = controller.onInit(onInitResponse)
          response.statusCode shouldBe DatabaseError.OnWrite.status()
        }
      }
    }
  }

}