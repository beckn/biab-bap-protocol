package org.beckn.one.sandbox.bap.message.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.beckn.protocol.schemas.Default

data class ItemDao @Default constructor(
  val id: String? = null,
  val parentItemId: String? = null,
  val descriptor: DescriptorDao? = null,
  val price: PriceDao? = null,
  val categoryId: String? = null,
  val locationId: String? = null,
  val time: TimeDao? = null,
  val matched: Boolean? = null,
  val related: Boolean? = null,
  val recommended: Boolean? = null,
  val rateable: Boolean? = null,
  val tags: Map<String, String>? = null,

  @param:JsonProperty("./ondc-returnable")
  val ondcReturnable: Boolean? = null,
  @param:JsonProperty("./ondc-seller_pickup_return")
  val ondcSellerPickupReturn: Boolean? = null,
  @param:JsonProperty("./ondc-return_window")
  val ondcReturnWindow: String? = null,
  @param:JsonProperty("./ondc-cancellable")
  val ondcCancellable: Boolean? = null,
  @param:JsonProperty("./ondc-time_to_ship")
  val ondcTimeToShip: String? = null,
  @param:JsonProperty("./ondc-manufacturer_or_packer_name")
  val ondcManufacturerOrPackerName: String? = null,
  @param:JsonProperty("./ondc-manufacturer_or_packer_address")
  val ondcManufacturerOrPackerAddress: String? = null,
  @param:JsonProperty("./ondc-common_or_generic_name_of_commodity")
  val ondcCommonOrGenericNameOfCommodity: String? = null,
  @param:JsonProperty("./ondc-multiple_products_name_number_or_qty")
  val ondcMultipleProductsNameNumberOrQty: String? = null,
  @param:JsonProperty("./ondc-net_quantity_or_measure_of_commodity_in_pkg")
  val ondcNetQuantityOrMeasureOfCommodityInPkg: String? = null,
  @param:JsonProperty("../ondc-month_year_of_manufacture_packing_import")
  val ondcMonthYearOfManufacturePackingImport: String? = null,
  @param:JsonProperty("./ondc-imported_product_country_of_origin")
  val ondcImportedProductCountryOfOrigin: String? = null,
  @param:JsonProperty("./ondc-contact_details_consumer_care")
  val ondcContactDetailsConsumerCare: String? = null,

  @param:JsonProperty("./ondc-ingredients_info")
  val ondcIngredientsInfo: String? = null,
  @param:JsonProperty("./ondc-additives_info")
  val ondcAdditivesInfo: String? = null,
//  @param:JsonProperty("./ondc-manufacturer_or_packer_name")
//  val ondcManufacturerOrPackerName: String? = null,
//  @param:JsonProperty("./ondc-manufacturer_or_packer_address")
//  val ondcManufacturerOrPackerAddress: String? = null,
  @param:JsonProperty("./ondc-brand_owner_name")
  val ondcBrandOwnerName: String? = null,
  @param:JsonProperty("./ondc-brand_owner_address")
  val ondcBrandOwnerAddress: String? = null,
  @param:JsonProperty("./ondc-brand_owner_FSSAI_logo")
  val ondcBrandOwnerFssaiLogo: String? = null,
  @param:JsonProperty("./ondc-brand_owner_FSSAI_license_no")
  val ondcBrandOwnerFssaiLicenseNo: String? = null,
  @param:JsonProperty("./ondc-other_FSSAI_license_no")
  val ondcOtherFssaiLicenseNo: String? = null,
  @param:JsonProperty("../ondc-net_quantity")
  val ondcNetQuantity: String? = null,
  @param:JsonProperty("./ondc-importer_name")
  val ondcImporterName: String? = null,
  @param:JsonProperty("./ondc-importer_address")
  val ondcImporterAddress: String? = null,
  @param:JsonProperty("./ondc-importer_FSSAI_logo")
  val ondcImporterFssaiLogo: String? = null,
  @param:JsonProperty("./ondc-importer_FSSAI_license_no")
  val ondcImporterFssaiLicenseNo: String? = null,
//  @param:JsonProperty("./ondc-imported_product_country_of_origin")
//  val ondcImportedProductCountryOfOrigin: String? = null,
  @param:JsonProperty("./ondc-other_importer_name")
  val ondcOtherImporterName: String? = null,
  @param:JsonProperty("./ondc-other_importer_address")
  val ondcOtherImporterAddress: String? = null,
  @param:JsonProperty("./ondc-other_premises")
  val ondcOtherPremises: String? = null,
  @param:JsonProperty("./ondc-other_importer_country_of_origin")
  val ondcOtherImporterCountryOfOrigin: String? = null,
//  @param:JsonProperty("./ondc-contact_details_consumer_care")
//  val ondcContactDetailsConsumerCare: String? = null,
)
