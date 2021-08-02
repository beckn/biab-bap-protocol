package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default

data class OnRatingMessageFeedbackDao @Default constructor(
  val id: String? = null,
  val descriptor: String? = null,
  val parentId: String? = null,
)