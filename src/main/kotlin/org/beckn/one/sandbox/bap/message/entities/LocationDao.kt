package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default

data class LocationDao @Default constructor(
  val id: String? = null,
  val descriptor: DescriptorDao? = null,
  val gps: String? = null,
  val address: AddressDao? = null,
  val stationCode: String? = null,
  val city: CityDao? = null,
  val country: CountryDao? = null,
  val circle: CircleDao? = null,
  val polygon: String? = null,
  val `3dspace`: String? = null
)

data class CityDao @Default constructor(
  val name: String? = null,
  val code: String? = null
)

data class CountryDao @Default constructor(
  val name: String? = null,
  val code: String? = null
)

data class CircleDao @Default constructor(
  val radius: ScalarDao? = null
)

data class ScalarDao @Default constructor(
  val value: java.math.BigDecimal,
  val unit: String,
  val type: Type? = null,
  val estimatedValue: java.math.BigDecimal? = null,
  val computedValue: java.math.BigDecimal? = null,
  val range: ScalarRangeDao? = null
) {

  enum class Type(val value: String) {
    CONSTANT("CONSTANT"),
    VARIABLE("VARIABLE");
  }
}

data class ScalarRangeDao @Default constructor(
  val min: java.math.BigDecimal? = null,
  val max: java.math.BigDecimal? = null
)