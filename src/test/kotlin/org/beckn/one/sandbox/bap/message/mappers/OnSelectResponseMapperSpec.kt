package org.beckn.one.sandbox.bap.message.mappers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.message.entities.OnSelectDao
import org.beckn.one.sandbox.bap.message.entities.OnSelectMessageDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolOnSelectMessageSelectedFactory
import org.beckn.protocol.schemas.ProtocolOnSelect
import org.beckn.protocol.schemas.ProtocolOnSelectMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
class OnSelectResponseMapperSpec @Autowired constructor(
  private val mapper: OnSelectResponseMapper
): DescribeSpec() {

  private val protocolResponse = ProtocolOnSelect(
    context = ProtocolContextFactory.fixed,
    message = ProtocolOnSelectMessage(
      ProtocolOnSelectMessageSelectedFactory.create(1, 2)
    )
  )

  private val entityResponse = OnSelectDao(
    context = ProtocolContextFactory.fixedAsEntity(protocolResponse.context!!),
    message = OnSelectMessageDao(
      ProtocolOnSelectMessageSelectedFactory.createAsEntity(protocolResponse.message?.order)
    )
  )
  init {
      describe("OnSelectResponseMapper") {
        it("should map properties from entity to schema") {
          mapper.protocolToEntity(protocolResponse) shouldBe entityResponse
        }

        it("should map properties from schema to entity") {
          mapper.entityToProtocol(entityResponse) shouldBe protocolResponse
        }
      }
  }
}