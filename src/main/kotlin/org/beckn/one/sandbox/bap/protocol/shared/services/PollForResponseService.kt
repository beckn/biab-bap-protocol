package org.beckn.one.sandbox.bap.protocol.shared.services

import arrow.core.Either
import arrow.core.flatMap
import org.beckn.one.sandbox.bap.errors.HttpError
import org.beckn.one.sandbox.bap.message.services.MessageService
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.protocol.schemas.ProtocolResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class PollForResponseService<Protocol: ProtocolResponse> constructor(
  private val messageService: MessageService,
  private val responseStorageService: ResponseStorageService<Protocol>
) {
  private val log: Logger = LoggerFactory.getLogger(this::class.java)

  open fun findResponses(messageId: String): Either<HttpError, List<Protocol>> {
    log.info("Got fetch request for message id: {}", messageId)
    return responseStorageService.findByMessageId(messageId)
  }

}