package org.beckn.one.sandbox.bap.message.repositories

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import org.beckn.one.sandbox.bap.configurations.DatabaseConfiguration
import org.beckn.one.sandbox.bap.configurations.TestDatabaseConfiguration
import org.beckn.one.sandbox.bap.message.entities.CatalogDao
import org.beckn.one.sandbox.bap.message.entities.ContextDao
import org.beckn.one.sandbox.bap.message.entities.OnSearchDao
import org.beckn.one.sandbox.bap.message.entities.OnSearchMessageDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolCatalogFactory
import org.springframework.boot.test.context.SpringBootTest
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

@SpringBootTest(classes = [TestDatabaseConfiguration::class, DatabaseConfiguration::class])
class BecknResponseRepositorySpec constructor(
  val repo: BecknResponseRepository<OnSearchDao>
) : DescribeSpec() {
  init {
    describe("Generic Repository") {

      context("for SearchResponse") {
        val searchResponse = OnSearchDao(
          context = context,
          message = OnSearchMessageDao(CatalogDao())
        )

        it("should fetch responses by message id") {
          repo.clear()
          repo.insertMany(
            listOf(
              searchResponse,
              searchResponse,
              searchResponse.copy(context = context.copy(messageId = "123"))
            )
          )
          repo.findByMessageId(context.messageId).size shouldBeExactly 2
        }
      }
    }
  }

  private val fixedClock = Clock.fixed(
    Instant.parse("2018-11-30T18:35:24.00Z"),
    ZoneId.of("UTC")
  )

  private val context = ContextDao(
    domain = "LocalRetail",
    country = "IN",
    action = ContextDao.Action.SEARCH,
    city = "Pune",
    coreVersion = "0.9.1-draft03",
    bapId = "http://host.bap.com",
    bapUri = "http://host.bap.com",
    transactionId = "222",
    messageId = "222",
    timestamp =  OffsetDateTime.now(fixedClock)
  )
}
