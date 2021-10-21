package org.beckn.one.sandbox.bap.message.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.beckn.protocol.schemas.Default

data class QuotationDao @Default constructor(
  val price: PriceDao? = null,
  val breakup: List<QuotationBreakupDao>? = null,
  val ttl: String? = null
)


data class QuotationBreakupDao @Default constructor(
  val title: String? = null,
  val price: PriceDao? = null
) {

  enum class Type(val value: String) {
    ITEM("item"),
    OFFER("offer"),
    @JsonProperty("add-on") ADDON("add-on"),
    FULFILLMENT("fulfillment");
  }
}