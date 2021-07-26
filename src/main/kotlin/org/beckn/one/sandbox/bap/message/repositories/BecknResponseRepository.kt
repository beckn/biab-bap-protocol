package org.beckn.one.sandbox.bap.message.repositories

import com.mongodb.client.MongoCollection
import org.beckn.one.sandbox.bap.message.entities.BecknResponseDao
import org.beckn.one.sandbox.bap.message.entities.ContextDao
import org.litote.kmongo.div
import org.litote.kmongo.eq

open class BecknResponseRepository<R : BecknResponseDao>(
  collection: MongoCollection<R>
) : GenericRepository<R>(collection) {

  fun findByMessageId(id: String): List<R> = findAll(BecknResponseDao::context / ContextDao::messageId eq id)

}