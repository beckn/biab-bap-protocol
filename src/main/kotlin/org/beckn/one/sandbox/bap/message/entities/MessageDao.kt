package org.beckn.one.sandbox.bap.message.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.Clock
import java.time.LocalDateTime

data class MessageDao(
  @field:BsonId private val _id: Id<String> = newId(),
  val id: String,
  val type: Type,
  @field:JsonIgnore val clock: Clock = Clock.systemUTC(),
  val created: LocalDateTime = LocalDateTime.now(clock),
  val updated: LocalDateTime = LocalDateTime.now(clock)
) {
  enum class Type {
    Search,
    Select,
    Init,
    Confirm,
    Track,
    Cancel,
    Status,
  }
}

