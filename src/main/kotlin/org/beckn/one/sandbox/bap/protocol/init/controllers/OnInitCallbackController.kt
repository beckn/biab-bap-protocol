package org.beckn.one.sandbox.bap.protocol.init.controllers

import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractCallbackController
import org.beckn.protocol.schemas.ProtocolOnInit
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class OnInitCallbackController(
  store: ResponseStorageService<ProtocolOnInit>
): AbstractCallbackController<ProtocolOnInit>(store) {

  @PostMapping(
    "protocol/v1/on_init",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  fun onInit(@RequestBody initResponse: ProtocolOnInit) = onCallback(initResponse)

}