package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.CatalogDao
import org.beckn.one.sandbox.bap.message.entities.PriceDao
import org.beckn.one.sandbox.bap.message.entities.ProviderCatalogDao
import org.beckn.protocol.schemas.ProtocolCatalog
import org.beckn.protocol.schemas.ProtocolPrice
import org.beckn.protocol.schemas.ProtocolProviderCatalog

object ProtocolCatalogFactory {
  fun create(index: Int = 1): ProtocolCatalog {
    return ProtocolCatalog(
      bppProviders = listOf(
        ProtocolProviderCatalog(
          id = IdFactory.forProvider(index),
          descriptor = ProtocolDescriptorFactory.create("Retail-provider", IdFactory.forProvider(index)),
          categories = IdFactory.forCategory(IdFactory.forProvider(index), 1).map { ProtocolCategoryFactory.create(it) },
          items = IdFactory.forItems(IdFactory.forProvider(index), 1).map { ProtocolItemFactory.create(it) }
        )
      )
    )
  }

  fun createAsEntity(protocol: ProtocolCatalog?) = protocol?.let {
    CatalogDao(
      bppProviders = it.bppProviders?.map { bpp ->
        ProviderCatalogDao(
          id = bpp.id,
          descriptor = ProtocolDescriptorFactory.createAsEntity(bpp.descriptor),
          categories = bpp.categories?.mapNotNull { c -> ProtocolCategoryFactory.createAsEntity(c) },
          items = bpp.items?.map { i -> ProtocolItemFactory.createAsEntity(i) }
        )
      }
    )
  }
}


object ProtocolPriceFactory {

  fun create() = ProtocolPrice(
    currency = "Rupees",
    value = "99",
    minimumValue = "100",
    estimatedValue = "101",
    computedValue = "102",
    offeredValue = "103",
    listedValue = "104",
    maximumValue = "105"
  )

  fun createAsEntity(protocol: ProtocolPrice?) = protocol?.let {
    PriceDao(
      currency = protocol.currency,
      value = protocol.value,
      minimumValue = protocol.minimumValue,
      estimatedValue = protocol.estimatedValue,
      computedValue = protocol.computedValue,
      offeredValue = protocol.offeredValue,
      listedValue = protocol.listedValue,
      maximumValue = protocol.maximumValue
    )
  }

}