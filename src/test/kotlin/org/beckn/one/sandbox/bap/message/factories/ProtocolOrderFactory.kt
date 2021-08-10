package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.*
import org.beckn.protocol.schemas.*
import java.time.OffsetDateTime

object ProtocolOrderFactory {
  fun create(id: Int): ProtocolOrder {
    val itemIds = IdFactory.forItems(IdFactory.forProvider(id), 3)
    return ProtocolOrder(
      provider = ProtocolSelectMessageSelectedProvider(
        id = IdFactory.forProvider(id),
        locations = listOf(ProtocolSelectMessageSelectedProviderLocations(IdFactory.forLocation(id)))
      ),
      items = itemIds.mapNotNull {
        ProtocolSelectMessageSelectedItems(
          id = it,
          quantity = ProtocolItemQuantityAllocated(count = 2)
        )
      },
      addOns = listOf(),
      offers = listOf(),
      billing = ProtocolBillingFactory.create(),
      fulfillment = ProtocolFulfillmentFactory.create(id),
      quote = ProtocolQuotationFactory.quoteForItems(itemIds),
      payment = ProtocolPaymentFactory.create(),
      id = IdFactory.forProvider(id),
      state = "IN_PROGRESS",
      createdAt = OffsetDateTime.now(fixedClock),
      updatedAt = OffsetDateTime.now(fixedClock)
    )
  }

  fun createAsEntity(protocol: ProtocolOrder?) = protocol.let {
    OrderDao(
      provider = protocol?.provider?.let { p ->
        SelectMessageSelectedProviderDao(
          id = p.id,
          locations = p.locations?.map { l -> SelectMessageSelectedProviderLocationsDao(l.id) }
        )
      }!!,
      items = protocol.items.map { i ->
        SelectMessageSelectedItemsDao(
          id = i.id,
          quantity = ItemQuantityAllocatedDao(count = i.quantity.count)
        )
      },
      addOns = listOf(),
      offers = listOf(),
      billing = ProtocolBillingFactory.createAsEntity(it?.billing)!!,
      fulfillment = ProtocolFulfillmentFactory.createAsEntity(it?.fulfillment)!!,
      quote = ProtocolQuotationFactory.createAsEntity(it?.quote)!!,
      payment = ProtocolPaymentFactory.createAsEntity(it?.payment)!!,
      id = it?.id,
      state = it?.state,
      createdAt = it?.createdAt,
      updatedAt = it?.updatedAt
    )
  }
}