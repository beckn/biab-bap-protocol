package org.beckn.one.sandbox.bap.common.factories

import org.beckn.protocol.schemas.*

class ResponseFactory {
  companion object {
    fun getDefault(context: ProtocolContext) = ProtocolAckResponse(
      context = context, message = ResponseMessage(ProtocolAck(ResponseStatus.ACK))
    )
  }
}