package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default

data class OfferDao @Default constructor(
  val id: String? = null,
  val descriptor: DescriptorDao? = null,
  val locationIds: List<String>? = null,
  val categoryIds: List<String>? = null,
  val itemIds: List<String>? = null,
  val time: TimeDao? = null
)
