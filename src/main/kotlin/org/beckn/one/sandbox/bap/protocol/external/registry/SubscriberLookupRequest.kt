package org.beckn.one.sandbox.bap.protocol.external.registry

import org.beckn.one.sandbox.bap.protocol.external.domains.Subscriber

data class SubscriberLookupRequest(
  val subscriber_id: String? = null,
  val type: Subscriber.Type,
  val domain: String,
  val country: String,
  val city: String
)