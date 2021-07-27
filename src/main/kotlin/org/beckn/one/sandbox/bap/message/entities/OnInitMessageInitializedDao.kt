package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default

data class OnInitMessageInitializedDao @Default constructor(
  val provider: OnInitMessageInitializedProviderDao? = null,
  val providerLocation: OnInitMessageInitializedProviderLocationDao? = null,
  val items: List<OnInitMessageInitializedItemsDao>? = null,
  val addOns: List<OnInitMessageInitializedAddOnsDao>? = null,
  val offers: List<OnInitMessageInitializedOffersDao>? = null,
  val billing: BillingDao? = null,
  val fulfillment: FulfillmentDao? = null,
  val quote: QuotationDao? = null,
  val payment: PaymentDao? = null
)

data class OnInitMessageInitializedProviderLocationDao @Default constructor(
  val id: String? = null
)

data class OnInitMessageInitializedProviderDao @Default constructor(
  val id: String? = null
)

data class OnInitMessageInitializedItemsDao @Default constructor(
  val id: String? = null,
  val quantity: ItemQuantityAllocatedDao? = null
)

// TODO: Example of inline declaration
data class ItemQuantityAllocatedDao @Default constructor(
  val count: Int? = null,
  val measure: ScalarDao? = null
)

data class OnInitMessageInitializedAddOnsDao @Default constructor(
  val id: String? = null
)

data class OnInitMessageInitializedOffersDao @Default constructor(
  val id: String? = null
)

