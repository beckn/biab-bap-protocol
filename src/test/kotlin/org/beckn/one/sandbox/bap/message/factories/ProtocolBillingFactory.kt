package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.AddressDao
import org.beckn.one.sandbox.bap.message.entities.BillingDao
import org.beckn.one.sandbox.bap.message.entities.OrganizationDao
import org.beckn.protocol.schemas.ProtocolAddress
import org.beckn.protocol.schemas.ProtocolBilling
import org.beckn.protocol.schemas.ProtocolOrganization

object ProtocolBillingFactory {

  fun create() = ProtocolBilling(
    name = "ICIC",
    phone = "9890098900",
    organization = ProtocolOrganization(name = "IC", cred = "ajsgdysdxasg!!"),
    address = ProtocolAddress(
      door = "A-11",
      building = "Vedanta",
      street = "High Street",
      areaCode = "435667"
    ),
    email = "abc@icic",
    time = ProtocolTimeFactory.fixedDays("Working Days")
  )

  fun createAsEntity(protocol: ProtocolBilling?) = protocol?.let {
    BillingDao(
      name = protocol.name,
      phone = protocol.phone,
      organization = protocol.organization?.let {
        OrganizationDao(name = it.name, cred = it.cred)
      },
      address = protocol.address?.let {
        AddressDao(
          door = it.door,
          building = it.building,
          street = it.street,
          areaCode = it.areaCode
        )
      },
      email = protocol.email,
      time = ProtocolTimeFactory.timeAsEntity(protocol.time)
    )
  }
}