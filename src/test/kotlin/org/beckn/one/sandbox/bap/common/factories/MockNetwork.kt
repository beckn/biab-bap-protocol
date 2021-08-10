package org.beckn.one.sandbox.bap.common.factories

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.beckn.one.sandbox.bap.common.City
import org.beckn.one.sandbox.bap.common.Country
import org.beckn.one.sandbox.bap.common.Domain
import org.beckn.one.sandbox.bap.protocol.external.registry.SubscriberDto

object MockNetwork {

  val registry = WireMockServer(4000)
  val registryBppLookupApi = WireMockServer(4010)
  val retailBengaluruBg = WireMockServer(4001)
  val anotherRetailBengaluruBg = WireMockServer(4002)
  val deliveryPuneBg = WireMockServer(4003)
  val retailBengaluruBpp = WireMockServer(4004)
  val anotherRetailBengaluruBpp = WireMockServer(4005)
  val deliveryPuneBpp = WireMockServer(4006)

  fun startAllSubscribers() {
    registry.start()
    registryBppLookupApi.start()
    retailBengaluruBg.start()
    anotherRetailBengaluruBg.start()
    deliveryPuneBg.start()
    retailBengaluruBpp.start()
    anotherRetailBengaluruBpp.start()
    deliveryPuneBpp.start()
  }

  fun resetAllSubscribers() {
    registry.resetAll()
    registryBppLookupApi.resetAll()
    retailBengaluruBg.resetAll()
    anotherRetailBengaluruBg.resetAll()
    deliveryPuneBg.resetAll()
    retailBengaluruBpp.resetAll()
    anotherRetailBengaluruBpp.resetAll()
    deliveryPuneBpp.resetAll()
  }

  fun getAllSubscribers(): List<SubscriberDto> {
    return listOf(
      getRetailBengaluruBg(),
      getAnotherRetailBengaluruBg(),
      getDeliveryPuneBg(),
      getRetailBengaluruBpp(),
      getAnotherRetailBengaluruBpp(),
      getDeliveryPuneBpp(),
    )
  }

  fun getAllGateways(): List<SubscriberDto> {
    return listOf(
      getRetailBengaluruBg(),
      getAnotherRetailBengaluruBg(),
      getDeliveryPuneBg(),
    )
  }

  fun getRetailBengaluruBg() = createSubscriberDto(1, retailBengaluruBg)

  private fun getAnotherRetailBengaluruBg() = createSubscriberDto(2, anotherRetailBengaluruBg)

  private fun getDeliveryPuneBg() = createSubscriberDto(
    number = 3, mockServer = deliveryPuneBg, city = City.Pune.value, domain = Domain.Delivery.value
  )

  fun getRetailBengaluruBpp() = createSubscriberDto(
    number = 4, mockServer = retailBengaluruBpp, type = SubscriberDto.Type.BPP
  )

  private fun getAnotherRetailBengaluruBpp() = createSubscriberDto(
    number = 5, mockServer = anotherRetailBengaluruBpp, type = SubscriberDto.Type.BPP
  )

  private fun getDeliveryPuneBpp() = createSubscriberDto(
    number = 6,
    mockServer = deliveryPuneBpp,
    city = City.Pune.value,
    domain = Domain.Delivery.value,
    type = SubscriberDto.Type.BPP
  )

  private fun createSubscriberDto(
    number: Int,
    mockServer: WireMockServer,
    type: SubscriberDto.Type = SubscriberDto.Type.BG,
    domain: String = Domain.LocalRetail.value,
    city: String = City.Bengaluru.value,
    country: String = Country.India.value,
    status: SubscriberDto.Status = SubscriberDto.Status.SUBSCRIBED
  ) = SubscriberDtoFactory.getDefault(
    number = number,
    baseUrl = mockServer.baseUrl(),
    type = type,
    domain = domain,
    city = city,
    country = country,
    status = status,
  )

  fun stubBppLookupApi(bppApi: WireMockServer, objectMapper: ObjectMapper) {
    registryBppLookupApi
      .stubFor(
        WireMock.post("/lookup")
          .withRequestBody(WireMock.matchingJsonPath("$.subscriber_id", WireMock.equalTo(bppApi.baseUrl())))
          .willReturn(WireMock.okJson(getSubscriberForBpp(objectMapper, bppApi)))
      )
  }

  private fun getSubscriberForBpp(objectMapper: ObjectMapper, bppApi: WireMockServer) =
    objectMapper.writeValueAsString(
      listOf(
        SubscriberDtoFactory.getDefault(
          subscriber_id = bppApi.baseUrl(),
          baseUrl = bppApi.baseUrl(),
          type = SubscriberDto.Type.BPP,
        )
      )
    )

}