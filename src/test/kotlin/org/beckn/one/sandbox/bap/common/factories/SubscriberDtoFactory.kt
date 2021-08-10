package org.beckn.one.sandbox.bap.common.factories

import org.beckn.one.sandbox.bap.common.City
import org.beckn.one.sandbox.bap.common.Country
import org.beckn.one.sandbox.bap.common.Domain
import org.beckn.one.sandbox.bap.protocol.external.registry.SubscriberDto
import java.time.Clock

class SubscriberDtoFactory {
  companion object {
    fun getDefault(
      number: Int = 1,
      subscriber_id: String = "subscriber-$number.network-$number.org",
      baseUrl: String = "https://subscriber_$number.com",
      type: SubscriberDto.Type = SubscriberDto.Type.BG,
      domain: String = Domain.LocalRetail.value,
      city: String = City.Bengaluru.value,
      country: String = Country.India.value,
      status: SubscriberDto.Status = SubscriberDto.Status.SUBSCRIBED,
      signing_public_key: String = "signing_public_key $number",
      encr_public_key: String = "encr_public_key $number",
      clock: Clock = Clock.systemUTC()
    ) = SubscriberDto(
      subscriber_id = subscriber_id,
      subscriber_url = baseUrl,
      type = type,
      domain = domain,
      city = city,
      country = country,
      status = status,
      signing_public_key = signing_public_key,
      encr_public_key = encr_public_key,
      clock = clock
    )
  }
}