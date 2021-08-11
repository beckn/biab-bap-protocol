package org.beckn.one.sandbox.bap.protocol.shared.security

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldNot
import okhttp3.*
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
internal class SignRequestInterceptorSpec @Autowired constructor(
  interceptor: SignRequestInterceptor
) : DescribeSpec() {

  private val content = """
    {"context":{"domain":"test","country":"string","city":"string","action":"on_support","core_version":"string","bap_id":"string","bap_uri":"string","bpp_id":"string","bpp_uri":"string","transaction_id":"string","message_id":"string","timestamp":"2021-03-30T12:25:31.333Z","key":"string","ttl":"string"},"message":{"phone":"string","email":"user@example.com","uri":"string"}}
  """.trimIndent()
  private val requestBody = RequestBody.create(MediaType.get("application/json"), content)

  private val request = Request.Builder()
    .method("POST", requestBody)
    .url("http://localhost:1111").build()
  private val response = Response.Builder()
    .code(200)
    .request(request)
    .protocol(Protocol.HTTP_1_1)
    .message("").build()

  private val mockChain = mock<Interceptor.Chain> {
    on { request() }.thenReturn(request)
    on { proceed(any()) }.thenReturn(response)
  }
  private val signedRequestCaptor = argumentCaptor<Request>()

  init {
    describe("SignRequestInterceptor") {
      interceptor.intercept(mockChain)
      it("should add authorization header to request") {
        verify(mockChain).proceed(signedRequestCaptor.capture())
        signedRequestCaptor.allValues.first().header(Authorization.HEADER_NAME) shouldNot beNull()
      }
    }

  }
}