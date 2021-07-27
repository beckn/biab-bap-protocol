package org.beckn.one.sandbox.bap.message.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.beckn.protocol.schemas.Default

data class OnTrackMessageTrackingDao @Default constructor(
  val url: String? = null,
  val status: TrackingStatusDao? = null,
) {
  enum class TrackingStatusDao {
    @JsonProperty("active")
    Active,

    @JsonProperty("inactive")
    Inactive
  }
}
