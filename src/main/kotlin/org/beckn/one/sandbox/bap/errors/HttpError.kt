package org.beckn.one.sandbox.bap.errors

import org.beckn.protocol.schemas.ProtocolError
import org.beckn.protocol.schemas.ResponseMessage
import org.springframework.http.HttpStatus

interface HttpError {
  fun status(): HttpStatus
  fun message(): ResponseMessage
  fun error(): ProtocolError
}
