package org.beckn.one.sandbox.bap.message.mappers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.message.entities.ContextDao
import org.beckn.one.sandbox.bap.protocol.ProtocolVersion
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.one.sandbox.bap.schemas.factories.UuidFactory
import org.beckn.protocol.schemas.ProtocolContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
class ContextMapperSpec @Autowired constructor(
  private val uuidFactory: UuidFactory,
  private val contextFactory: ContextFactory,
  private val mapper: ContextMapper,
  @Value("\${context.domain}") private val domain: String,
  @Value("\${context.city}") private val city: String,
  @Value("\${context.country}") private val country: String,
  @Value("\${context.bap_id}") private val bapId: String,
  @Value("\${context.bap_uri}") private val bapUri: String,
) : DescribeSpec() {
  init {
    describe("ContextMapper") {
      it("should map all fields from schema to entity") {
        val schema = contextFactory.create()

        val mappedEntity = mapper.fromSchema(schema)

        mappedEntity.domain shouldBe schema.domain
        mappedEntity.country shouldBe schema.country
        mappedEntity.city shouldBe schema.city
        mappedEntity.action shouldBe ContextDao.Action.SEARCH
        mappedEntity.coreVersion shouldBe schema.coreVersion
        mappedEntity.bapId shouldBe schema.bapId
        mappedEntity.bapUri shouldBe schema.bapUri
        mappedEntity.bppId shouldBe schema.bppId
        mappedEntity.bppUri shouldBe schema.bppUri
        mappedEntity.transactionId shouldBe schema.transactionId
        mappedEntity.messageId shouldBe schema.messageId
        mappedEntity.timestamp shouldBe schema.timestamp
        mappedEntity.key shouldBe schema.key
        mappedEntity.ttl shouldBe schema.ttl
      }

      it("should map all fields from entity to schema") {
        val entity = ContextDao(
          domain = domain,
          country = country,
          city = city,
          action = ContextDao.Action.SEARCH,
          coreVersion = ProtocolVersion.V0_9_1.value,
          bapId = bapId,
          bapUri = bapUri,
          transactionId = uuidFactory.create(),
          messageId = uuidFactory.create(),
        )

        val mappedSchema = mapper.toSchema(entity)

        mappedSchema.domain shouldBe entity.domain
        mappedSchema.country shouldBe entity.country
        mappedSchema.city shouldBe entity.city
        mappedSchema.action shouldBe ProtocolContext.Action.SEARCH
        mappedSchema.coreVersion shouldBe entity.coreVersion
        mappedSchema.bapId shouldBe entity.bapId
        mappedSchema.bapUri shouldBe entity.bapUri
        mappedSchema.bppId shouldBe entity.bppId
        mappedSchema.bppUri shouldBe entity.bppUri
        mappedSchema.transactionId shouldBe entity.transactionId
        mappedSchema.messageId shouldBe entity.messageId
        mappedSchema.timestamp shouldBe entity.timestamp
        mappedSchema.key shouldBe entity.key
        mappedSchema.ttl shouldBe entity.ttl
      }

    }
  }
}