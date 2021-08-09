package org.beckn.one.sandbox.bap.protocol.fulfillment.track.controllers

import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractPollForResponseController
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolOnTrack
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PollTrackResponseController @Autowired constructor(
  responseService: PollForResponseService<ProtocolOnTrack>,
  contextFactory: ContextFactory
) : AbstractPollForResponseController<ProtocolOnTrack>(responseService, contextFactory) {

  @GetMapping("protocol/response/v1/on_track")
  @ResponseBody
  fun getTrackResponses(messageId: String) = findResponses(messageId)

}