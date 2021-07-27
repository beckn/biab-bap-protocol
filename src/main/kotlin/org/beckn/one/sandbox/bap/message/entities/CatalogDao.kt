package org.beckn.one.sandbox.bap.message.entities

import org.beckn.protocol.schemas.Default
import java.time.LocalDateTime

data class CatalogDao @Default constructor(
  val bppDescriptor: DescriptorDao? = null,
  val bppProviders: List<ProviderCatalogDao>? = null,
  val bppCategories: List<CategoryDao>? = null,
  val exp: LocalDateTime? = null,
  val bppId: String? = null,
)

data class ProviderCatalogDao @Default constructor(
  val id: String? = null,
  val descriptor: DescriptorDao? = null,
  val locations: List<LocationDao>? = null,
  val categories: List<CategoryDao>? = null,
  val items: List<ItemDao>? = null,
  val tags: Map<String, String>? = null,
  val exp: LocalDateTime? = null,
  val matched: Boolean? = null
)
