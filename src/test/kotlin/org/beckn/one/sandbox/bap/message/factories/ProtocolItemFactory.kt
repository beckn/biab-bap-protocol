package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.ItemDao
import org.beckn.protocol.schemas.ProtocolItem

object ProtocolItemFactory {

  fun create(itemId: String) = ProtocolItem(
    id = "Item_$itemId",
    descriptor = ProtocolDescriptorFactory.create("provider-$itemId-item", itemId),
    price = ProtocolPriceFactory.create(),
    categoryId = "provider-$itemId-category-$itemId",
    tags = mapOf("item-tag1" to "item-value1"),
    matched = true,
    related = true,
    recommended = true,
    time = ProtocolTimeFactory.fixedRange("range")
  )

  fun createAsEntity(protocol: ProtocolItem) = ItemDao(
    id = protocol.id,
    descriptor = ProtocolDescriptorFactory.createAsEntity(protocol.descriptor),
    price = ProtocolPriceFactory.createAsEntity(protocol.price),
    categoryId = protocol.categoryId,
    tags = protocol.tags,
    matched = protocol.matched,
    related = protocol.related,
    recommended = protocol.recommended,
    time = ProtocolTimeFactory.timeAsEntity(protocol.time)
  )
}