package org.beckn.one.sandbox.bap.message.mappers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.message.entities.OnRatingDao
import org.beckn.one.sandbox.bap.message.entities.OnRatingMessageDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOnRatingMessageFeedbackFactory
import org.beckn.protocol.schemas.ProtocolOnRating
import org.beckn.protocol.schemas.ProtocolOnRatingMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
class OnRatingResponseMapperSpec @Autowired constructor(
  private val mapper: OnRatingResponseMapper
) : DescribeSpec() {

  private val protocolResponse = ProtocolOnRating(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnRatingMessage(
      ProtocolOnRatingMessageFeedbackFactory.create(1)
    )
  )

  private val entityResponse = OnRatingDao(
    context = ProtocolContextFactory.fixedAsEntity(protocolResponse.context!!),
    message = OnRatingMessageDao(
      ProtocolOnRatingMessageFeedbackFactory.createAsEntity(protocolResponse.message?.feedback)
    )
  )

  init {
    describe("OnRatingResponseMapper") {
      it("should map properties from entity to schema") {
        mapper.protocolToEntity(protocolResponse) shouldBe entityResponse
      }

      it("should map properties from schema to entity") {
        mapper.entityToProtocol(entityResponse) shouldBe protocolResponse
      }
    }
  }
}