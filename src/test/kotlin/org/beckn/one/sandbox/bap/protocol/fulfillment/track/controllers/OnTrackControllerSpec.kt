package org.beckn.one.sandbox.bap.protocol.fulfillment.track.controllers

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.OnTrackDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.protocol.schemas.ProtocolOnTrack
import org.beckn.protocol.schemas.ProtocolOnTrackMessage
import org.beckn.protocol.schemas.ProtocolOnTrackMessageTracking
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
internal class OnTrackControllerSpec @Autowired constructor(
  @Autowired
  private val mockMvc: MockMvc,
  @Autowired
  private val mapper: ObjectMapper,
  @Autowired
  private val onTrackResponseRepo: BecknResponseRepository<OnTrackDao>,
) : DescribeSpec() {
  private val postOnTrackUrl = "/protocol/v1/on_track"
  val onTrackResponse = ProtocolOnTrack(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnTrackMessage(
      tracking = ProtocolOnTrackMessageTracking(
        url = "www.tracking.com",
        status = ProtocolOnTrackMessageTracking.ProtocolTrackingStatus.Active
      )
    )
  )

  init {

    describe("Protocol OnTrack API") {

      context("when posted to with a valid response") {
        onTrackResponseRepo.clear()
        val postOnTrackResponse = mockMvc
          .perform(
            post(postOnTrackUrl)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .content(mapper.writeValueAsBytes(onTrackResponse))
          )

        it("should respond with status as 200") {
          postOnTrackResponse.andExpect(status().isOk)
        }

        it("should save on track response in db") {
          onTrackResponseRepo.findByMessageId(onTrackResponse.context!!.messageId).size shouldBeExactly 1
        }
      }

      context("when error occurs when processing request") {
        val mockService = mock<ResponseStorageService<ProtocolOnTrack>> {
          onGeneric { save(onTrackResponse) }.thenReturn(Either.Left(DatabaseError.OnWrite))
        }
        val controller = OnTrackController(mockService)

        it("should respond with internal server error") {
          val response = controller.onTrack(onTrackResponse)
          response.statusCode shouldBe DatabaseError.OnWrite.status()
        }
      }
    }
  }

}