package org.beckn.one.sandbox.bap.message.entities

import org.beckn.one.sandbox.bap.Default


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
  val selected: OnSelectMessageSelectedDao? = null
)

data class OnInitDao @Default constructor(
  override val context: ContextDao,
  val message: OnInitMessageDao? = null,
  override val error: ErrorDao? = null
) : BecknResponseDao

data class OnInitMessageDao @Default constructor(
  val initialized: OnInitMessageInitializedDao? = null
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
