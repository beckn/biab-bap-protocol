package org.beckn.one.sandbox.bap.common.factories

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.beckn.one.sandbox.bap.common.City
import org.beckn.one.sandbox.bap.common.Country
import org.beckn.one.sandbox.bap.common.Domain
import org.beckn.one.sandbox.bap.protocol.ProtocolVersion
import org.beckn.protocol.schemas.ProtocolContext
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.beckn.one.sandbox.bap.schemas.factories.UuidFactory
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal class ContextFactorySpec : DescribeSpec() {
  init {
    describe("Create") {
      it("should create new context") {
        val uuidFactory = mock(UuidFactory::class.java)
        val fixedUtcClock = Clock.fixed(Instant.now(), ZoneId.of("UTC"))
        val transactionId = "8521bc1a-31ae-4720-b78a-c0ff929b2b44"
        val messageId = "3f6555f9-96c7-4555-8000-29a06be3c931"
        `when`(uuidFactory.create()).thenReturn(transactionId, messageId)
        val contextFactory = ContextFactory(
          Domain.Delivery.value,
          City.Pune.value,
          Country.India.value,
          "bap 1",
          "bap1.com",
          uuidFactory,
          fixedUtcClock
        )

        val context = contextFactory.create()

        context.domain shouldBe Domain.Delivery.value
        context.country shouldBe Country.India.value
        context.city shouldBe City.Pune.value
        context.action shouldBe ProtocolContext.Action.SEARCH
        context.coreVersion shouldBe ProtocolVersion.V0_9_1.value
        context.bapId shouldBe "bap 1"
        context.bapUri shouldBe "bap1.com"
        context.bppId shouldBe null
        context.bppUri shouldBe null
        context.transactionId shouldBe transactionId
        context.messageId shouldBe messageId
        context.clock shouldBe fixedUtcClock
        context.key shouldBe null
        context.ttl shouldBe null
      }
    }
  }
}