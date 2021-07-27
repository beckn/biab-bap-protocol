package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default

data class AddressDao @Default constructor(
  val door: String? = null,
  val name: String? = null,
  val building: String? = null,
  val street: String? = null,
  val locality: String? = null,
  val ward: String? = null,
  val city: String? = null,
  val state: String? = null,
  val country: String? = null,
  val areaCode: String? = null
)
