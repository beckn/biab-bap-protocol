package org.beckn.one.sandbox.bap.protocol.select.controllers

import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractPollForResponseController
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolOnSelect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PollSelectResponseController @Autowired constructor(
  responseService: PollForResponseService<ProtocolOnSelect>,
  contextFactory: ContextFactory
): AbstractPollForResponseController<ProtocolOnSelect>(responseService, contextFactory) {

  @GetMapping("protocol/response/v1/on_select")
  @ResponseBody
  fun getSelectResponses(messageId: String) = findResponses(messageId)

}