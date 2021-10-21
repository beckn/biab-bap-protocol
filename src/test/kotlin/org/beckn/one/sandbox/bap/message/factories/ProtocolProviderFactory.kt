package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.ProviderDao
import org.beckn.protocol.schemas.ProtocolProvider

object ProtocolProviderFactory {

  fun create(id: Int): ProtocolProvider {
    val providerId = IdFactory.forProvider(id)
    return ProtocolProvider(
      id = providerId,
      descriptor = ProtocolDescriptorFactory.create("Retail-provider", providerId),
      time = ProtocolTimeFactory.fixedTimestamp("fixed-time"),
      locations = listOf(
        ProtocolLocationFactory.cityLocation(1),
        ProtocolLocationFactory.cityLocation(2).copy(city = ProtocolCityFactory.pune)
      ),
      tags = mapOf("key 1" to "value 1"),
      category_id = ProtocolCategoryFactory.create(providerId).id
    )
  }

  fun createAsEntity(protocol: ProtocolProvider?) = protocol?.let {
    ProviderDao(
      id = protocol.id,
      descriptor = ProtocolDescriptorFactory.createAsEntity(protocol.descriptor),
      time = ProtocolTimeFactory.timeAsEntity(protocol.time),
      locations = protocol.locations?.mapNotNull { ProtocolLocationFactory.locationEntity(it) },
      tags = protocol.tags,
      category_id = protocol.category_id
    )
  }
}