package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.QuotationDao
import org.beckn.one.sandbox.bap.message.entities.QuotationBreakupDao
import org.beckn.protocol.schemas.ProtocolQuotation
import org.beckn.protocol.schemas.ProtocolQuotationBreakup

object ProtocolQuotationFactory {

  fun quoteForItems(itemIds: List<String>) = ProtocolQuotation(
    price = ProtocolPriceFactory.create(),
    breakup = itemIds.map { ProtocolQuotationBreakupFactory.forItem(it) },
    ttl = "30"
  )

  fun createAsEntity(protocol: ProtocolQuotation?) = protocol?.let {
    QuotationDao(
      price = ProtocolPriceFactory.createAsEntity(protocol.price),
      breakup = protocol.breakup?.mapNotNull { ProtocolQuotationBreakupFactory.createAsEntity(it) },
      ttl = protocol.ttl
    )
  }
}

object ProtocolQuotationBreakupFactory {

  private fun create(id: String, type: ProtocolQuotationBreakup.Type) = ProtocolQuotationBreakup(
    price = ProtocolPriceFactory.create()
  )

  fun forItem(itemId: String) = create(itemId, ProtocolQuotationBreakup.Type.ITEM)
  fun forAddon(itemId: String) = create(itemId, ProtocolQuotationBreakup.Type.ADDON)
  fun forFulfilment(itemId: String) = create(itemId, ProtocolQuotationBreakup.Type.FULFILLMENT)
  fun forOffer(itemId: String) = create(itemId, ProtocolQuotationBreakup.Type.OFFER)

  fun createAsEntity(protocol: ProtocolQuotationBreakup?) = protocol?.let {
    QuotationBreakupDao(
      price = ProtocolPriceFactory.createAsEntity(protocol.price)
    )
  }
}