package org.beckn.one.sandbox.bap.message.mappers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.message.entities.DescriptorDao
import org.beckn.one.sandbox.bap.message.entities.OnSearchDao
import org.beckn.one.sandbox.bap.message.entities.OnSearchMessageDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolCatalogFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.protocol.schemas.ProtocolOnSearch
import org.beckn.protocol.schemas.ProtocolOnSearchMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
class OnSearchResponseMapperSpec @Autowired constructor(
  private val mapper: OnSearchResponseMapper
) : DescribeSpec() {
  private val fixedClock = Clock.fixed(
    Instant.parse("2018-11-30T18:35:24.00Z"),
    ZoneId.of("UTC")
  )

  val protocolSearchResponse = ProtocolOnSearch(
    message = ProtocolOnSearchMessage(
      ProtocolCatalogFactory.create(1)
    ),
    context = ProtocolContextFactory.fixed
  )

  private val entitySearchResponse =  OnSearchDao(
    message = OnSearchMessageDao(
      ProtocolCatalogFactory.createAsEntity(protocolSearchResponse.message?.catalog)
    ),
    context = ProtocolContextFactory.fixedAsEntity(protocolSearchResponse.context!!)
  )
  init {
    describe("SearchResponseMapper") {
      it("should map all fields from schema to entity") {
        val mappedEntity = mapper.protocolToEntity(protocolSearchResponse)
        mappedEntity shouldBe entitySearchResponse
      }

      it("should map all fields from entity to schema") {
        val mappedSchema = mapper.entityToProtocol(entitySearchResponse)
        mappedSchema shouldBe protocolSearchResponse
      }
    }
  }

  fun descriptor(type: String, index: Int) = DescriptorDao(
    name = "$type-$index name",
    code = "$type-$index code",
    symbol = "$type-$index symbol",
    shortDesc = "A short description about $type-$index",
    longDesc = "A long description about $type-$index",
    images = listOf("uri:https://$type-$index-image-1.com", "uri:https://$type-$index-image-2.com"),
    audio = "$type-$index-image-audio-file-path",
    threeDRender = "$type-$index-3d"
  )
}