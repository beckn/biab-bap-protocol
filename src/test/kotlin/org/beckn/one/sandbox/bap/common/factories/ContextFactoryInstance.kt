package org.beckn.one.sandbox.bap.common.factories

import org.beckn.one.sandbox.bap.common.City
import org.beckn.one.sandbox.bap.common.Country
import org.beckn.one.sandbox.bap.common.Domain
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.one.sandbox.bap.schemas.factories.UuidFactory
import java.time.Clock

class ContextFactoryInstance {
  companion object {
    fun create(uuidFactory: UuidFactory = UuidFactory(), clock: Clock = Clock.systemUTC()) = ContextFactory(
      domain = Domain.LocalRetail.value,
      city = City.Bengaluru.value,
      country = Country.India.value,
      bapId = "beckn_in_a_box_bap",
      bapUrl = "beckn_in_a_box_bap.com",
      uuidFactory = uuidFactory,
      clock = clock
    )
  }
}