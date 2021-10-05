package org.beckn.one.sandbox.bap.message.mappers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.message.entities.OnTrackDao
import org.beckn.one.sandbox.bap.message.entities.OnTrackMessageDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOnTrackMessageTrackingFactory
import org.beckn.protocol.schemas.ProtocolOnTrack
import org.beckn.protocol.schemas.ProtocolOnTrackMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
class OnTrackResponseMapperSpec @Autowired constructor(
  private val mapper: OnTrackResponseMapper
) : DescribeSpec() {

  private val protocolResponse = ProtocolOnTrack(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnTrackMessage(
      ProtocolOnTrackMessageTrackingFactory.create(1)
    )
  )

  private val entityResponse = OnTrackDao(
    context = ProtocolContextFactory.fixedAsEntity(protocolResponse.context!!),
    message = OnTrackMessageDao(
      ProtocolOnTrackMessageTrackingFactory.createAsEntity(protocolResponse.message.tracking)
    )
  )

  init {
    describe("OnTrackResponseMapper") {
      it("should map properties from entity to schema") {
        mapper.protocolToEntity(protocolResponse) shouldBe entityResponse
      }

      it("should map properties from schema to entity") {
        mapper.entityToProtocol(entityResponse) shouldBe protocolResponse
      }
    }
  }
}