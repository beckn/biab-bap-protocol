package org.beckn.one.sandbox.bap.protocol.cancel.controllers

import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractPollForResponseController
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolOnCancel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PollCancelResponseController @Autowired constructor(
  responseService: PollForResponseService<ProtocolOnCancel>,
  contextFactory: ContextFactory
) : AbstractPollForResponseController<ProtocolOnCancel>(responseService, contextFactory) {

  @GetMapping("protocol/response/v1/on_cancel")
  @ResponseBody
  fun getCancelResponses(messageId: String) = findResponses(messageId)
}