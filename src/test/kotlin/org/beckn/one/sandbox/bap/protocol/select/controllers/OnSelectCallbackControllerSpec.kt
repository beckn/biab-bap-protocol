package org.beckn.one.sandbox.bap.protocol.select.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.OnSelectDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOnSelectMessageSelectedFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.protocol.schemas.ProtocolOnSelect
import org.beckn.protocol.schemas.ProtocolOnSelectMessage
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
internal class OnSelectCallbackControllerSpec @Autowired constructor(
  @Autowired
  private val mockMvc: MockMvc,
  @Autowired
  private val mapper: ObjectMapper,
  @Autowired
  private val onSelectResponseRepo: BecknResponseRepository<OnSelectDao>,
) : DescribeSpec() {
  private val postOnSelectUrl = "/protocol/v1/on_select"
  val onSelectResponse = ProtocolOnSelect(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnSelectMessage(
      order = ProtocolOnSelectMessageSelectedFactory.create(1, 2)
    )
  )

  init {

    describe("Protocol OnSelect API") {

      context("when posted to with a valid response") {
        onSelectResponseRepo.clear()
        val postOnSelectResponse = mockMvc
          .perform(
            post(postOnSelectUrl)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsBytes(onSelectResponse))
          )

        it("should respond with status as 200") {
          postOnSelectResponse.andExpect(status().isOk)
        }

        it("should save on select response in db") {
          onSelectResponseRepo.findByMessageId(onSelectResponse.context!!.messageId).size shouldBeExactly 1
        }
      }

      context("when error occurs when processing request") {
        val mockService = mock<ResponseStorageService<ProtocolOnSelect>> {
          onGeneric { save(onSelectResponse) }.thenReturn(Either.Left(DatabaseError.OnWrite))
        }
        val controller = OnSelectCallbackController(mockService)

        it("should respond with internal server error") {
          val response = controller.onSelect(onSelectResponse)
          response.statusCode shouldBe DatabaseError.OnWrite.status()
        }
      }
    }
  }

}