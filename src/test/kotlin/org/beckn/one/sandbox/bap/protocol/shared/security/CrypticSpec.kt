package org.beckn.one.sandbox.bap.protocol.shared.security

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CrypticSpec : DescribeSpec() {

  private val b64PrivateKey = "8j0Menby6+O4kUkE2yDJw4u0pqWmAdlftsROXlbKHVGHlb0U6bkLc857vBBKHCL5TDsLxsw7mqJFI0cyC7/a8A=="
  private val b64PublicKey = "h5W9FOm5C3POe7wQShwi+Uw7C8bMO5qiRSNHMgu/2vA="
  private val sampleBody = """
    {"context":{"domain":"test","country":"string","city":"string","action":"on_support","core_version":"string","bap_id":"string","bap_uri":"string","bpp_id":"string","bpp_uri":"string","transaction_id":"string","message_id":"string","timestamp":"2021-03-30T12:25:31.333Z","key":"string","ttl":"string"},"message":{"phone":"string","email":"user@example.com","uri":"string"}}
  """.trimIndent()
  private val sampleAuthorization = """
    Signature keyId="MOCK_SUB_ID|key1|xed25519",algorithm="xed25519",created="1627447288",expires="1627450888",headers="(created) (expires) digest",signature="hrEaAq8fpChrKBuevWTRu1dl2evXcoDjuaSPCg+olGXqnR3NA7NyqqQIJJ5m50beEzp/YOAxceATlPEYtaCiDg=="
  """.trimIndent()
  private val authorization = Authorization.parse(sampleAuthorization)!!

  init {
    describe("Cryptic") {

      it("should return true if request is not tampered with") {
        Cryptic.verify(authorization, b64PublicKey, sampleBody) shouldBe true
      }

      it("should return false if body is tampered with") {
        val tampered = sampleBody + "tamper"
        Cryptic.verify(authorization, b64PublicKey, tampered) shouldBe false
      }

      it("should verify self generated signature") {
        val selfAuthorization = authorization.copy(signature = Cryptic.sign(b64PrivateKey, sampleBody, 1627447288, 1627450888))
        Cryptic.verify(selfAuthorization, b64PublicKey, sampleBody) shouldBe true
      }
    }
  }
}
