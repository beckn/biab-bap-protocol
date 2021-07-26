package org.beckn.one.sandbox.bap.protocol.select.controllers

import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractCallbackController
import org.beckn.protocol.schemas.ProtocolOnSelect
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class OnSelectCallbackController(
  store: ResponseStorageService<ProtocolOnSelect>
) : AbstractCallbackController<ProtocolOnSelect>(store) {

  @PostMapping(
    "protocol/v1/on_select",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  fun onSelect(@RequestBody selectResponse: ProtocolOnSelect) = onCallback(selectResponse)

}