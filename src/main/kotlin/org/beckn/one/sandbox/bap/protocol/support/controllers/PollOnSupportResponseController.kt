package org.beckn.one.sandbox.bap.protocol.support.controllers

import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractPollForResponseController
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolOnSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PollOnSupportResponseController @Autowired constructor(
  responseService: PollForResponseService<ProtocolOnSupport>,
  contextFactory: ContextFactory
) : AbstractPollForResponseController<ProtocolOnSupport>(responseService, contextFactory) {

  @RequestMapping("protocol/response/v1/on_support")
  @ResponseBody
  fun getSupportResponses(messageId: String) = findResponses(messageId)

}