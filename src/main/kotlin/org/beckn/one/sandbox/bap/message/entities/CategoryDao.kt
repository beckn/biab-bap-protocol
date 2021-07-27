package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default
import java.time.LocalDateTime

data class CategoryDao @Default constructor(
  val _id: String? = null,
  val id: String? = null,
  val parentCategoryId: String? = null,
  val descriptor: DescriptorDao? = null,
  val time: LocalDateTime? = null,
  val tags: Map<String, String>? = null
)