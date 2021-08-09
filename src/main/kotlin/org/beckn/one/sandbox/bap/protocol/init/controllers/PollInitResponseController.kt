package org.beckn.one.sandbox.bap.protocol.init.controllers

import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractPollForResponseController
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolOnInit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PollInitResponseController @Autowired constructor(
  responseService: PollForResponseService<ProtocolOnInit>,
  contextFactory: ContextFactory
): AbstractPollForResponseController<ProtocolOnInit>(responseService, contextFactory) {

  @GetMapping("protocol/response/v1/on_init")
  @ResponseBody
  fun getInitResponses(messageId: String) = findResponses(messageId)

}