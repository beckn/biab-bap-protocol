package org.beckn.one.sandbox.bap.message.mappers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.message.entities.OnConfirmDao
import org.beckn.one.sandbox.bap.message.entities.OnConfirmMessageDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOrderFactory
import org.beckn.protocol.schemas.ProtocolOnConfirm
import org.beckn.protocol.schemas.ProtocolOnConfirmMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
class OnConfirmResponseMapperSpec @Autowired constructor(
  private val mapper: OnConfirmResponseMapper
): DescribeSpec() {

  private val protocolResponse = ProtocolOnConfirm(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnConfirmMessage(
      ProtocolOrderFactory.create(1)
    )
  )

  private val entityResponse = OnConfirmDao(
    context = ProtocolContextFactory.fixedAsEntity(protocolResponse.context!!),
    message = OnConfirmMessageDao(
      ProtocolOrderFactory.createAsEntity(protocolResponse.message.order)
    )
  )
  init {
      describe("OnConfirmResponseMapper") {
        it("should map properties from entity to schema") {
          mapper.protocolToEntity(protocolResponse) shouldBe entityResponse
        }

        it("should map properties from schema to entity") {
          mapper.entityToProtocol(entityResponse) shouldBe protocolResponse
        }
      }
  }
}