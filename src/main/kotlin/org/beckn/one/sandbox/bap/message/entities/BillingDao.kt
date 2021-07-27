package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default


data class BillingDao  @Default constructor(
  val name: String,
  val phone: String,
  val organization: OrganizationDao? = null,
  val address: AddressDao? = null,
  val email: String? = null,
  val time: TimeDao? = null,
  val taxNumber: String? = null,
  val createdAt: java.time.OffsetDateTime? = null,
  val updatedAt: java.time.OffsetDateTime? = null
)

data class OrganizationDao  @Default constructor(
  val name: String? = null,
  val cred: String? = null
)

