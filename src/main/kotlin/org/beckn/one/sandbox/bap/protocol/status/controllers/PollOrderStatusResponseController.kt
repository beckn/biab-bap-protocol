package org.beckn.one.sandbox.bap.protocol.status.controllers

import org.beckn.one.sandbox.bap.protocol.shared.controllers.AbstractPollForResponseController
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolOnOrderStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PollOrderStatusResponseController @Autowired constructor(
  responseService: PollForResponseService<ProtocolOnOrderStatus>,
  contextFactory: ContextFactory
) : AbstractPollForResponseController<ProtocolOnOrderStatus>(responseService, contextFactory) {

  @GetMapping("protocol/response/v1/on_status")
  @ResponseBody
  fun getOrderStatusResponses(messageId: String) = findResponses(messageId)

}
