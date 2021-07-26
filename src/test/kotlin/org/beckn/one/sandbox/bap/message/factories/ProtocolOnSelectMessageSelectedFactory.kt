package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.OnSelectMessageSelectedDao
import org.beckn.protocol.schemas.ProtocolOnSelectMessageSelected

object ProtocolOnSelectMessageSelectedFactory {

  fun create(index: Int = 1, numberOfItems: Int = 2): ProtocolOnSelectMessageSelected {
    val provider = ProtocolProviderFactory.create(index)
    val itemIds = IdFactory.forItems(provider.id!!, numberOfItems)

    return ProtocolOnSelectMessageSelected(
      provider = ProtocolProviderFactory.create(index),
      providerLocation = null,
      items = itemIds.map { ProtocolOnSelectedItemFactory.create(it) },
      addOns = null,
      offers = null,
      quote = ProtocolQuotationFactory.quoteForItems(itemIds)
    )
  }

  fun createAsEntity(protocol: ProtocolOnSelectMessageSelected?) = protocol?.let {
    OnSelectMessageSelectedDao(
      provider = ProtocolProviderFactory.createAsEntity(protocol.provider),
      providerLocation = null,
      items = protocol.items?.map { ProtocolOnSelectedItemFactory.createAsEntity(it) },
      addOns = null,
      offers = null,
      quote = ProtocolQuotationFactory.createAsEntity(protocol.quote)
    )
  }

}

fun seqTill(n: Int) = generateSequence(
  seedFunction = { n },
  nextFunction = { (it + 1).takeIf { i -> i < n } }
).toList()


object IdFactory {

  fun forLocation(id: Int) = "location-$id"
  fun forFulfillment(id: Int) = "fulfillment-$id"
  fun forProvider(id: Int) = "provider-$id"
  fun forOrder(id: Int) = "order-$id"
  fun forCategory(providerId: String, numberOfCategories: Int) =
    seqTill(numberOfCategories).map { "$providerId-category-$it" }

  fun forItems(providerId: String, numberOfItems: Int) = seqTill(numberOfItems).map { "$providerId-item-$it" }

}