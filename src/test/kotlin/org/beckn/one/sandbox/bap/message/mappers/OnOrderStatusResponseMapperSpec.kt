package org.beckn.one.sandbox.bap.message.mappers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.message.entities.OnOrderStatusDao
import org.beckn.one.sandbox.bap.message.entities.OnOrderStatusMessageDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOrderFactory
import org.beckn.protocol.schemas.ProtocolOnOrderStatus
import org.beckn.protocol.schemas.ProtocolOnOrderStatusMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
class OnOrderStatusResponseMapperSpec @Autowired constructor(
  private val mapper: OnOrderStatusResponseMapper
) : DescribeSpec() {

  private val protocolResponse = ProtocolOnOrderStatus(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnOrderStatusMessage(
      order = ProtocolOrderFactory.create(1)
    )
  )

  private val entityResponse = OnOrderStatusDao(
    context = ProtocolContextFactory.fixedAsEntity(protocolResponse.context!!),
    message = OnOrderStatusMessageDao(
      ProtocolOrderFactory.createAsEntity(protocolResponse.message?.order)
    )
  )

  init {
    describe("OnOrderStatusResponseMapper") {
      it("should map properties from entity to schema") {
        mapper.protocolToEntity(protocolResponse) shouldBe entityResponse
      }

      it("should map properties from schema to entity") {
        mapper.entityToProtocol(entityResponse) shouldBe protocolResponse
      }
    }
  }
}