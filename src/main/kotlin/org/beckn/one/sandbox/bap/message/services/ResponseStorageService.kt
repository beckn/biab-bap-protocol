package org.beckn.one.sandbox.bap.message.services

import arrow.core.Either
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.BecknResponseDao
import org.beckn.one.sandbox.bap.message.mappers.GenericResponseMapper
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.protocol.schemas.ProtocolResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface ResponseStorageService<Proto : ProtocolResponse> {
  fun save(protoResponse: Proto): Either<DatabaseError.OnWrite, Proto>
  fun findByMessageId(id: String): Either<DatabaseError.OnRead, List<Proto>>
}

class ResponseStorageServiceImpl<Proto : ProtocolResponse, Entity : BecknResponseDao> constructor(
  val responseRepo: BecknResponseRepository<Entity>,
  val mapper: GenericResponseMapper<Proto, Entity>
) : ResponseStorageService<Proto> {
  private val log: Logger = LoggerFactory.getLogger(this::class.java)

  override fun save(protoResponse: Proto): Either<DatabaseError.OnWrite, Proto> =
    Either
      .catch {
        log.info("Saving protocol response: {}", protoResponse)
        responseRepo.insertOne(mapper.protocolToEntity(protoResponse))
      }
      .bimap(
        rightOperation = { protoResponse },
        leftOperation = {
          log.error("Exception while saving search response", it)
          DatabaseError.OnWrite
        }
      )

  override fun findByMessageId(id: String): Either<DatabaseError.OnRead, List<Proto>> = Either
    .catch { responseRepo.findByMessageId(id) }
    .map { toSchema(it) }
    .mapLeft { e ->
      log.error("Exception while fetching search response", e)
      DatabaseError.OnRead
    }

  private fun toSchema(allResponses: List<Entity>) =
    allResponses.map { response -> mapper.entityToProtocol(response) }

}