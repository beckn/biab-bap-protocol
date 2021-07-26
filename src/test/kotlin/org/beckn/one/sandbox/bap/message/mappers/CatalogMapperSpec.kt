package org.beckn.one.sandbox.bap.message.mappers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.message.entities.DescriptorDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolCatalogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
class CatalogMapperSpec @Autowired constructor(
  private val mapper: CatalogMapper
) : DescribeSpec() {

  init {
    describe("CatalogMapper") {
      it("should map all fields from schema to entity") {
        val catalog1Schema = ProtocolCatalogFactory.create()
        val mappedEntity = mapper.schemaToEntity(catalog1Schema)
        mappedEntity shouldBe ProtocolCatalogFactory.createAsEntity(catalog1Schema)
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