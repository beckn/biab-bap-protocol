package org.beckn.one.sandbox.bap.message.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.beckn.protocol.schemas.Default

data class PriceDao @Default constructor(
  val currency: String? = null,
  val value: String? = null,
  @JsonProperty("estimated_value") val estimatedValue: String? = null,
  @JsonProperty("computed_value") val computedValue: String? = null,
  @JsonProperty("listed_value") val listedValue: String? = null,
  @JsonProperty("offered_value") val offeredValue: String? = null,
  @JsonProperty("minimum_value") val minimumValue: String? = null,
  @JsonProperty("maximum_value") val maximumValue: String? = null,
)
