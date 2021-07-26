package org.beckn.one.sandbox.bap.protocol.fulfillment.track.controllers

import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractCallbackController
import org.beckn.protocol.schemas.ProtocolOnTrack
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class OnTrackController(store: ResponseStorageService<ProtocolOnTrack>) :
  AbstractCallbackController<ProtocolOnTrack>(store) {

  @PostMapping(
    "protocol/v1/on_track",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  fun onTrack(@RequestBody trackResponse: ProtocolOnTrack) = onCallback(trackResponse)

}
