package org.beckn.one.sandbox.bap.protocol.support.controllers

import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractCallbackController
import org.beckn.protocol.schemas.ProtocolOnSupport
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OnSupportCallbackController(
  store: ResponseStorageService<ProtocolOnSupport>
) : AbstractCallbackController<ProtocolOnSupport>(store) {

  @PostMapping(
    "protocol/v1/on_support",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  fun onSupport(@RequestBody supportResponse: ProtocolOnSupport) = onCallback(supportResponse)
}