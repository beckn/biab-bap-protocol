package org.beckn.one.sandbox.bap.protocol.rating.controllers

import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractPollForResponseController
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolOnRating
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PollRatingResponseController @Autowired constructor(
  responseService: PollForResponseService<ProtocolOnRating>,
  contextFactory: ContextFactory
) : AbstractPollForResponseController<ProtocolOnRating>(responseService, contextFactory) {

  @GetMapping("protocol/response/v1/on_rating")
  @ResponseBody
  fun getTrackResponses(messageId: String) = findResponses(messageId)

}