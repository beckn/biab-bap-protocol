package org.beckn.one.sandbox.bap.protocol.status.controllers

import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractCallbackController
import org.beckn.protocol.schemas.ProtocolOnOrderStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class OnStatusController(store: ResponseStorageService<ProtocolOnOrderStatus>) :
  AbstractCallbackController<ProtocolOnOrderStatus>(store) {

  @PostMapping(
    "protocol/v1/on_status",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  fun onStatus(@RequestBody statusResponse: ProtocolOnOrderStatus) = onCallback(statusResponse)

}
