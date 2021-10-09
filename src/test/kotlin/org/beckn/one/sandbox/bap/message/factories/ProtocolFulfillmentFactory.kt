package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.*
import org.beckn.protocol.schemas.*
import java.time.LocalDate
import java.time.OffsetDateTime

object ProtocolFulfillmentFactory {

  fun create(id: Int) = ProtocolFulfillment(
    id = IdFactory.forFulfillment(id),
    type = "Delivery",
    state = ProtocolState(
      descriptor = ProtocolDescriptorFactory.create("Fulfillment", IdFactory.forFulfillment(id)),
      updatedAt = OffsetDateTime.now(fixedClock),
      updatedBy = "Beck"
    ),
    tracking = false,
    agent = ProtocolPersonFactory.create(),
    vehicle = ProtocolVehicle(
      category = "Sedan",
      capacity = 4,
      make = "Toyota",
      model = "Etios",
      color = "White",
      registration = "MH99ZZ9876"
    ),
    start = ProtocolFulfillmentStart(
      location = ProtocolLocationFactory.addressLocation(1),
      time = ProtocolTimeFactory.fixedTimestamp("start-on"),
      contact = ProtocolContact(phone = "9890098900", email = "ab@gmail.com")
    ),
    end = ProtocolFulfillmentEnd(
      location = ProtocolLocationFactory.addressLocation(1),
      time = ProtocolTimeFactory.fixedTimestamp("start-on"),
      contact = ProtocolContact(phone = "9890098900", email = "ab@gmail.com")
    ),
    customer = ProtocolCustomer(
      person = ProtocolPersonFactory.create()
    ),
  )

  fun createAsEntity(protocol: ProtocolFulfillment?) = protocol?.let {
    FulfillmentDao(
      id = it.id,
      type = it.type,
      state = it.state?.let { s ->
        StateDao(
          descriptor = ProtocolDescriptorFactory.createAsEntity(s.descriptor),
          updatedAt = s.updatedAt,
          updatedBy = s.updatedBy
        )
      },
      tracking = it.tracking,
      agent = ProtocolPersonFactory.createAsEntity(it.agent),
      vehicle = it.vehicle?.let { v ->
        VehicleDao(
          category = v.category,
          capacity = v.capacity,
          make = v.make,
          model = v.model,
          color = v.color,
          size = v.size,
          variant = v.variant,
          energyType = v.energyType,
          registration = v.registration
        )
      },
      start = it.start?.let { f ->
        FulfillmentStartDao(
          location = ProtocolLocationFactory.locationEntity(f.location),
          time = ProtocolTimeFactory.timeAsEntity(f.time),
          contact = f.contact?.let { c ->
            ContactDao(phone = c.phone, email = c.email, tags = c.tags)
          }
        )
      },
      end = it.end?.let { f ->
        FulfillmentEndDao(
          location = ProtocolLocationFactory.locationEntity(f.location),
          time = ProtocolTimeFactory.timeAsEntity(f.time),
          contact = f.contact?.let { c ->
            ContactDao(phone = c.phone, email = c.email, tags = c.tags)
          }
        )
      },
      customer = CustomerDao(
        person = ProtocolPersonFactory.createAsEntity(it.customer?.person)
      )
    )
  }
}


object ProtocolPersonFactory {

  fun create() = ProtocolPerson(
    name = "Ben Beckman",
    gender = "Male",
    image = "/image.jpg",
    dob = LocalDate.now(fixedClock),
    cred = "achgsg22@@"
  )

  fun createAsEntity(protocol: ProtocolPerson?) = protocol?.let {
    PersonDao(
      name = it.name,
      image = it.image,
      dob = it.dob,
      gender = it.gender,
      cred = it.cred,
      tags = it.tags
    )
  }

}
