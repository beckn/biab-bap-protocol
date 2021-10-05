package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default

data class FulfillmentDao  @Default constructor(
  val id: String? = null,
  val type: String? = null,
  val state: StateDao? = null,
  val tracking: Boolean? = null,
  val agent: PersonDao? = null,
  val vehicle: VehicleDao? = null,
  val start: FulfillmentStartDao? = null,
  val end: FulfillmentEndDao? = null,
  val customer: CustomerDao? = null,
  val tags: Map<String, String>? = null
)


data class CustomerDao @Default constructor(
  val person: PersonDao? = null,
  val contact: ContactDao? = null
)

data class StateDao  @Default constructor(
  val descriptor: DescriptorDao? = null,
  val updatedAt: java.time.OffsetDateTime? = null,
  val updatedBy: String? = null
)

data class PersonDao  @Default constructor(
  val name: String? = null,
  val image: String? = null,
  val dob: java.time.LocalDate? = null,
  val gender: String? = null,
  val cred: String? = null,
  val tags: Map<String, String>? = null
)

data class VehicleDao  @Default constructor(
  val category: String? = null,
  val capacity: Int? = null,
  val make: String? = null,
  val model: String? = null,
  val size: String? = null,
  val variant: String? = null,
  val color: String? = null,
  val energyType: String? = null,
  val registration: String? = null
)
// TODO Similar classes
data class FulfillmentStartDao  @Default constructor(
  val location: LocationDao? = null,
  val time: TimeDao? = null,
  val instructions: DescriptorDao? = null,
  val contact: ContactDao? = null
)

// TODO Similar classes
data class FulfillmentEndDao  @Default constructor(
  val location: LocationDao? = null,
  val time: TimeDao? = null,
  val instructions: DescriptorDao? = null,
  val contact: ContactDao? = null
)


data class ContactDao  @Default constructor(
  val phone: String? = null,
  val email: String? = null,
  val tags: Map<String, String>? = null
)