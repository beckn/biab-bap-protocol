package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.*
import org.beckn.protocol.schemas.*

object ProtocolOnInitMessageInitializedFactory {

  fun create(id: Int, numberOfItems: Int): ProtocolOnInitMessageInitialized {
    val providerId = IdFactory.forProvider(id)
    val itemIds = IdFactory.forItems(providerId, numberOfItems)
    return ProtocolOnInitMessageInitialized(
      provider = ProtocolOnInitMessageInitializedProvider(id = providerId),
      providerLocation = ProtocolOnInitMessageInitializedProviderLocation(id = "location-$id"),
      items = itemIds.map {
        ProtocolOnInitMessageInitializedItems(
          id = it,
          quantity = ProtocolItemQuantityAllocated(count = 2)
        )
      },
      addOns = null,
      offers = null,
      billing = ProtocolBillingFactory.create(),
      fulfillment = ProtocolFulfillmentFactory.create(id),
      quote = ProtocolQuotationFactory.quoteForItems(itemIds)
    )
  }

  fun createAsEntity(protocol: ProtocolOnInitMessageInitialized?) = protocol?.let {
    OnInitMessageInitializedDao(
      provider = protocol.provider?.let { p -> OnInitMessageInitializedProviderDao(id = p.id) },
      providerLocation = protocol.providerLocation?.let { l -> OnInitMessageInitializedProviderLocationDao(id = l.id) },
      items = protocol.items?.map { i ->
        OnInitMessageInitializedItemsDao(
          id = i.id,
          quantity = i.quantity?.let { q ->
            ItemQuantityAllocatedDao(count = q.count)
          }
        )
      },
      addOns = null,
      offers = null,
      billing = ProtocolBillingFactory.createAsEntity(it.billing),
      fulfillment = ProtocolFulfillmentFactory.createAsEntity(it.fulfillment),
      quote = ProtocolQuotationFactory.createAsEntity(it.quote)
    )
  }
}