package org.beckn.one.sandbox.bap.message.services

import arrow.core.Either
import org.beckn.one.sandbox.bap.errors.HttpError
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.MessageDao
import org.beckn.one.sandbox.bap.message.repositories.GenericRepository
import org.litote.kmongo.eq
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageService @Autowired constructor(
  private val messageRepository: GenericRepository<MessageDao>
) {
  val log: Logger = LoggerFactory.getLogger(this::class.java)

  fun save(message: MessageDao): Either<HttpError, MessageDao> = Either
    .catch { messageRepository.insertOne(message) }
    .mapLeft { e ->
      log.error("Error when saving message to DB", e)
      DatabaseError.OnWrite
    }

  fun findById(id: String): Either<HttpError, MessageDao> {
    return Either
      .catch {
        return when (val message = messageRepository.findOne(MessageDao::id eq id)) {
          null -> Either.Left(DatabaseError.NotFound)
          else -> Either.Right(message)
        }
      }
      .mapLeft { e ->
        log.error("Error when fetching message by id $id", e)
        DatabaseError.OnRead
      }
  }
}
