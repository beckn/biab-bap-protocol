package org.beckn.one.sandbox.bap.message.services

import com.mongodb.MongoException
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.entities.MessageDao
import org.beckn.one.sandbox.bap.message.repositories.GenericRepository
import org.beckn.one.sandbox.bap.schemas.factories.UuidFactory
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class MessageServiceSpec : DescribeSpec() {

  init {
    it("should return error when unable to save message") {
      val message = MessageDao(id = UuidFactory().create(), type = MessageDao.Type.Search)
      val repository = mock<GenericRepository<MessageDao>> {
        onGeneric { insertOne(message) }.thenThrow(MongoException("Write error"))
      }
      val service = MessageService(repository)

      val saveResult = service.save(message)

      saveResult shouldBeLeft DatabaseError.OnWrite
    }

    it("should invoke repository to save message") {
      val message = MessageDao(id = UuidFactory().create(), type = MessageDao.Type.Search)
      val repository = mock<GenericRepository<MessageDao>> {
        onGeneric { insertOne(message) }.thenReturn(message)
      }
      val service = MessageService(repository)

      val saveResult = service.save(message)

      saveResult shouldBeRight message
      verify(repository).insertOne(message)
    }
  }
}