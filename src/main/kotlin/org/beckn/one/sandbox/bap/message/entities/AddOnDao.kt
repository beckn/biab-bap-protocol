package org.beckn.one.sandbox.bap.message.entities

import org.beckn.one.sandbox.bap.Default

data class AddOnDao @Default constructor(
  val id: String? = null,
  val descriptor: DescriptorDao? = null,
  val price: PriceDao? = null
)
