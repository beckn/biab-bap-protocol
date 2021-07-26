package org.beckn.one.sandbox.bap.message.entities

data class OfferDao (
  val id: String? = null,
  val descriptor: DescriptorDao? = null,
  val locationIds: List<String>? = null,
  val categoryIds: List<String>? = null,
  val itemIds: List<String>? = null,
  val time: TimeDao? = null
)
