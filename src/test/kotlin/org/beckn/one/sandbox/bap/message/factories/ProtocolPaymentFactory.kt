package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.PaymentDao
import org.beckn.protocol.schemas.ProtocolPayment
import java.net.URI

object ProtocolPaymentFactory {

  fun create() = ProtocolPayment(
    uri = URI("http://host.pay.co.in"),
    tlMethod = ProtocolPayment.TlMethod.POST,
    params = mapOf("nonce" to "JXNASN"),
    type = ProtocolPayment.Type.ONORDER,
    status = ProtocolPayment.Status.PAID,
    time = ProtocolTimeFactory.fixedTimestamp("Paid on")
  )

  fun createAsEntity(protocol: ProtocolPayment?) = protocol?.let {
    PaymentDao(
      uri = protocol.uri,
      tlMethod = PaymentDao.TlMethod.values().first { it.value == protocol.tlMethod?.value },
      params = protocol.params,
      type = PaymentDao.Type.values().first { it.value == protocol.type?.value },
      status = PaymentDao.Status.values().first { it.value == protocol.status?.value },
      time = ProtocolTimeFactory.timeAsEntity(protocol.time)
    )
  }
}