package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default
import org.beckn.protocol.schemas.ProtocolSchedule

data class TimeDao @Default constructor(
  val label: String? = null,
  val timestamp: java.time.Instant? = null,
  val duration: String? = null,
  val range: TimeRangeDao? = null,
  val days: String? = null,
  val schedule: ProtocolSchedule?= null

)

data class TimeRangeDao @Default constructor(
  val start: java.time.Instant? = null,
  val end: java.time.Instant? = null
)


