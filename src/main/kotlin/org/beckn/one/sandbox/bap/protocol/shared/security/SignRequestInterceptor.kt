package org.beckn.one.sandbox.bap.protocol.shared.security

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class SignRequestInterceptor @Autowired constructor(
  @Value("\${security.self.private_key}") private val b64PrivateKey: String,
  @Value("\${security.self.unique_key_id}") private val uniqueKeyId: String,
  @Value("\${context.bap_id}") private val subscriberId: String,
  @Value("\${context.ttl_seconds}") private val ttlInSeconds: String,
) : Interceptor {

  private val log: Logger = LoggerFactory.getLogger(this::class.java)

  override fun intercept(chain: Interceptor.Chain): Response {
    val original = chain.request()
    val authorization = createAuthorization(original)
    log.info("Auth recreated signing string : ", authorization.signature)
    val request = original.newBuilder()
      .header(Authorization.HEADER_NAME, authorization.headerString)
      .header(Authorization.ACCEPT,authorization.Accept)
      .build()
    return chain.proceed(request)
  }

  private fun getBodyContent(request: RequestBody): String {
    val buffer = Buffer()
    request.writeTo(buffer)
    return buffer.readUtf8()
  }

  private fun createAuthorization(request: Request): Authorization {
    val now = Instant.now()
    val created = now.epochSecond
    val expires = now.plusSeconds(ttlInSeconds.toLong()).epochSecond
    val bodyContent = getBodyContent(request.body()!!)
    log.info("Create Auth with body content : ", bodyContent)

    return Authorization.create(
      subscriberId = subscriberId,
      uniqueKeyId = uniqueKeyId,
      signature = Cryptic.sign(b64PrivateKey, bodyContent, created, expires),
      created = created,
      expires = expires
    )
  }

}