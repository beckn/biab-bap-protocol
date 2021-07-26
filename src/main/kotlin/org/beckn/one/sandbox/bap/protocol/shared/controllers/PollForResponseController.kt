package org.beckn.one.sandbox.bap.protocol.shared.controllers

import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.protocol.schemas.ProtocolErrorResponse
import org.beckn.protocol.schemas.ProtocolResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity

open class AbstractPollForResponseController<Protocol: ProtocolResponse>(
  private val responseService: PollForResponseService<Protocol>,
  private val contextFactory: ContextFactory
) {
  private val log: Logger = LoggerFactory.getLogger(this::class.java)

  fun findResponses(
    messageId: String
  ): ResponseEntity<List<ProtocolResponse>> = responseService
    .findResponses(messageId)
    .fold(
      {
        log.error("Error when finding search response by message id. Error: {}", it)
        ResponseEntity
          .status(it.status().value())
          .body(listOf(ProtocolErrorResponse(context = contextFactory.create(messageId = messageId), error = it.error())))
      },
      {
        log.info("Found responses for message {}", messageId)
        ResponseEntity.ok(it)
      }
    )
}