package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default


interface BecknResponseDao {
  val context: ContextDao
  val error: ErrorDao?
}

data class OnSearchDao @Default constructor(
  override val context: ContextDao,
  val message: OnSearchMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnSearchMessageDao @Default constructor(
  val catalog: CatalogDao? = null
)

data class OnSelectDao @Default constructor(
  override val context: ContextDao,
  val message: OnSelectMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnSelectMessageDao @Default constructor(
  val order: OnSelectMessageSelectedDao? = null
)

data class OnInitDao @Default constructor(
  override val context: ContextDao,
  val message: OnInitMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnInitMessageDao @Default constructor(
  val order: OnInitMessageInitializedDao? = null
)

data class OnConfirmDao @Default constructor(
  override val context: ContextDao,
  val message: OnConfirmMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnConfirmMessageDao @Default constructor(
  val order: OrderDao? = null
)

data class OnTrackDao @Default constructor(
  override val context: ContextDao,
  val message: OnTrackMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnTrackMessageDao @Default constructor(
  val tracking: OnTrackMessageTrackingDao? = null
)

data class OnSupportDao @Default constructor(
  override val context: ContextDao,
  val message: OnSupportMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnSupportMessageDao @Default constructor(
  val phone: String? = null,
  val email: String? = null,
  val uri: String? = null
)

data class OnRatingDao @Default constructor(
  override val context: ContextDao,
  val message: OnRatingMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnRatingMessageDao @Default constructor(
  val feedback: OnRatingMessageFeedbackDao? = null
)

data class OnCancelDao @Default constructor(
  override val context: ContextDao,
  val message: OnCancelMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnCancelMessageDao @Default constructor(
  val order: OrderDao? = null
)

data class OnOrderStatusDao @Default constructor(
  override val context: ContextDao,
  val message: OnOrderStatusMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnOrderStatusMessageDao @Default constructor(
  val order: OrderDao? = null
)
