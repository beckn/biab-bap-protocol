package org.beckn.one.sandbox.bap.protocol.search.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.OnSearchDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolCatalogFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.protocol.schemas.ProtocolOnSearch
import org.beckn.protocol.schemas.ProtocolOnSearchMessage
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
internal class OnSearchCallbackControllerSpec @Autowired constructor(
  @Autowired
  private val mockMvc: MockMvc,
  @Autowired
  private val mapper: ObjectMapper,
  @Autowired
  private val searchResponseRepo: BecknResponseRepository<OnSearchDao>,
) : DescribeSpec() {
  private val postOnSearchUrl = "/protocol/v1/on_search"

  val schemaSearchResponse = ProtocolOnSearch(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnSearchMessage(ProtocolCatalogFactory.create(2))
  )

  init {

    describe("Protocol Search API") {

      context("when posted to with a valid response") {
        searchResponseRepo.clear()
        val postSearchResponse = mockMvc
          .perform(
            post(postOnSearchUrl)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsBytes(schemaSearchResponse))
          )

        it("should respond with status as 200") {
          postSearchResponse.andExpect(status().isOk)
        }

        it("should save search response in db") {
          searchResponseRepo.findByMessageId(schemaSearchResponse.context!!.messageId).size shouldBeExactly 1
        }
      }

      context("when error occurs when processing request") {
        val mockService = mock<ResponseStorageService<ProtocolOnSearch>> {
          onGeneric { save(schemaSearchResponse) }.thenReturn(Either.Left(DatabaseError.OnWrite))
        }
        val controller = OnSearchCallbackController(mockService)

        it("should respond with internal server error") {
          val response = controller.onSearch(schemaSearchResponse)
          response.statusCode shouldBe DatabaseError.OnWrite.status()
        }
      }
    }
  }

}