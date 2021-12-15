package org.beckn.one.sandbox.bap.protocol.shared.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import org.beckn.one.sandbox.bap.common.factories.MockNetwork
import org.beckn.one.sandbox.bap.message.factories.*
import org.beckn.protocol.schemas.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import java.time.Instant

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  properties = ["beckn.security.enabled=true"]
)
@ActiveProfiles(value = ["test"])
class SignatureVerificationInterceptorSpec(
  @LocalServerPort port: Int,
  @Autowired restTemplate: TestRestTemplate,
  @Autowired mapper: ObjectMapper
) : DescribeSpec() {

  private val b64PrivateKey = "8j0Menby6+O4kUkE2yDJw4u0pqWmAdlftsROXlbKHVGHlb0U6bkLc857vBBKHCL5TDsLxsw7mqJFI0cyC7/a8A=="
  private val errorBody = """
    {"message":{"ack":{"status":"NACK"}}}
    """.trimIndent()

  val registryApi = MockNetwork.registryBppLookupApi

  init {
    registryApi.stubFor(
      WireMock.post("/lookup")
        .willReturn(WireMock.serverError())
    )
    registryApi.start()

    fun createRequest(
      requestBody: String,
      authHeaderName: String? = null,
      authString: String? = null
    ): HttpEntity<String> {
      val headers = HttpHeaders()
      headers.contentType = MediaType.APPLICATION_JSON
      val hName = authHeaderName ?: return HttpEntity(requestBody, headers)
      val hValue = authString ?: return HttpEntity(requestBody, headers)
      headers.set(hName, hValue)
      return HttpEntity(requestBody, headers)
    }

    fun signRequest(requestBody: String): Authorization {
      val created = Instant.now().toEpochMilli() / 1000
      val expires = Instant.now().plusSeconds(10000).toEpochMilli() / 1000
      return Authorization(
        keyId = "MOCK_SUB_ID|key1|xed25519",
        created = created,
        expires = expires,
        signature = Cryptic.sign(b64PrivateKey, requestBody, created, expires)
      )
    }

    describe("SignatureVerificationInterceptor for on search callback") {
      val schemaSearchResponse = ProtocolOnSearch(
        context = ProtocolContextFactory.fixed,
        message = ProtocolOnSearchMessage(ProtocolCatalogFactory.create(2))
      )
      val requestBody: String = mapper.writeValueAsString(schemaSearchResponse)

      context("when Authorization header is missing in search request") {
        val responseEntity = restTemplate
          .postForEntity("http://localhost:$port/protocol/v1/on_search", createRequest(requestBody), String::class.java)

        it("should return 401") {
          responseEntity.statusCode.value() shouldBe 401
        }

        it("should set WWW-Authenticate header") {
          responseEntity.headers["WWW-Authenticate"] shouldNot beNull()
        }

        it("should send nack in body") {
          responseEntity.body!! shouldBe errorBody
        }
      }

      context("when request has valid Authorization header") {
        val authorization = signRequest(requestBody)
        val request = createRequest(requestBody, "Authorization", authorization.headerString)
        val responseEntity = restTemplate
          .postForEntity("http://localhost:$port/protocol/v1/on_search", request, String::class.java)

        it("should forward to controller and response should be 200") {
          responseEntity.statusCode.value() shouldBe 200
        }
      }
    }

    describe("SignatureVerificationInterceptor for select callback APIs") {
      val schemaSelectResponse = ProtocolOnSelect(
        context = ProtocolContextFactory.fixed,
        message = ProtocolOnSelectMessage(ProtocolOnSelectMessageSelectedFactory.create(1, 2))
      )
      val requestBody: String = mapper.writeValueAsString(schemaSelectResponse)

      context("when Authorization header is missing in search request") {
        val responseEntity = restTemplate.postForEntity(
          "http://localhost:$port/protocol/v1/on_select",
          createRequest(requestBody),
          String::class.java
        )

        it("should return 401") {
          responseEntity.statusCode.value() shouldBe 401
        }

        it("should set WWW-Authenticate header") {
          responseEntity.headers["WWW-Authenticate"] shouldNot beNull()
        }

        it("should send nack in body") {
          responseEntity.body!! shouldBe errorBody
        }
      }

      context("when request has valid Authorization header") {
        val authorization = signRequest(requestBody)
        val request = createRequest(requestBody, "Authorization", authorization.headerString)
        val responseEntity = restTemplate
          .postForEntity("http://localhost:$port/protocol/v1/on_select", request, String::class.java)

        it("should forward to controller and response should be 200") {
          responseEntity.statusCode.value() shouldBe 200
        }
      }


    }

    describe("SignatureVerificationInterceptor for init callback APIs") {
      val schemaInitResponse = ProtocolOnInit(
        context = ProtocolContextFactory.fixed,
        message = ProtocolOnInitMessage(ProtocolOnInitMessageInitializedFactory.create(1, 2))
      )
      val requestBody: String = mapper.writeValueAsString(schemaInitResponse)

      context("when Authorization header is missing in search request") {
        val responseEntity = restTemplate.postForEntity(
          "http://localhost:$port/protocol/v1/on_init", createRequest(requestBody), String::class.java
        )

        it("should return 401") {
          responseEntity.statusCode.value() shouldBe 401
        }

        it("should set WWW-Authenticate header") {
          responseEntity.headers["WWW-Authenticate"] shouldNot beNull()
        }

        it("should send nack in body") {
          responseEntity.body!! shouldBe errorBody
        }
      }

      context("when request has valid Authorization header") {
        val authorization = signRequest(requestBody)
        val request = createRequest(requestBody, "Authorization", authorization.headerString)
        val responseEntity = restTemplate
          .postForEntity("http://localhost:$port/protocol/v1/on_init", request, String::class.java)

        it("should forward to controller and response should be 200") {
          responseEntity.statusCode.value() shouldBe 200
        }
      }
    }

    describe("SignatureVerificationInterceptor for confirm callback APIs") {
      val schemaConfirmResponse = ProtocolOnConfirm(
        context = ProtocolContextFactory.fixed,
        message = ProtocolOnConfirmMessage(ProtocolOrderFactory.create(1))
      )
      val requestBody: String = mapper.writeValueAsString(schemaConfirmResponse)

      context("when Authorization header is missing in search request") {
        val responseEntity = restTemplate.postForEntity(
          "http://localhost:$port/protocol/v1/on_confirm", createRequest(requestBody), String::class.java
        )

        it("should return 401") {
          responseEntity.statusCode.value() shouldBe 401
        }

        it("should set WWW-Authenticate header") {
          responseEntity.headers["WWW-Authenticate"] shouldNot beNull()
        }

        it("should send nack in body") {
          responseEntity.body!! shouldBe errorBody
        }
      }

      context("when request has valid Authorization header") {
        val authorization = signRequest(requestBody)
        val request = createRequest(requestBody, "Authorization", authorization.headerString)
        val responseEntity = restTemplate
          .postForEntity("http://localhost:$port/protocol/v1/on_confirm", request, String::class.java)

        it("should forward to controller and response should be 200") {
          responseEntity.statusCode.value() shouldBe 200
        }
      }
    }

    describe("SignatureVerificationInterceptor for track callback APIs") {
      val schemaTrackResponse = ProtocolOnTrack(
        context = ProtocolContextFactory.fixed,
        message = ProtocolOnTrackMessage(ProtocolOnTrackMessageTrackingFactory.create(1))
      )
      val requestBody: String = mapper.writeValueAsString(schemaTrackResponse)

      context("when Authorization header is missing in search request") {
        val responseEntity = restTemplate.postForEntity(
          "http://localhost:$port/protocol/v1/on_track", createRequest(requestBody), String::class.java
        )

        it("should return 401") {
          responseEntity.statusCode.value() shouldBe 401
        }

        it("should set WWW-Authenticate header") {
          responseEntity.headers["WWW-Authenticate"] shouldNot beNull()
        }

        it("should send nack in body") {
          responseEntity.body!! shouldBe errorBody
        }
      }

      context("when request has valid Authorization header") {
        val authorization = signRequest(requestBody)
        val request = createRequest(requestBody, "Authorization", authorization.headerString)
        val responseEntity = restTemplate
          .postForEntity("http://localhost:$port/protocol/v1/on_track", request, String::class.java)

        it("should forward to controller and response should be 200") {
          responseEntity.statusCode.value() shouldBe 200
        }
      }
    }
  }

}
