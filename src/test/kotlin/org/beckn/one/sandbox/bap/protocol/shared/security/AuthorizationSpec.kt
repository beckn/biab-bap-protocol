package org.beckn.one.sandbox.bap.protocol.shared.security

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class AuthorizationSpec : DescribeSpec() {
  private val sampleAuthorization = """
    Signature keyId="MOCK_SUB_ID|key1|ed25519",algorithm="ed25519",created="1627447288",expires="1627450888",headers="(created) (expires) digest",signature="hrEaAq8fpChrKBuevWTRu1dl2evXcoDjuaSPCg+olGXqnR3NA7NyqqQIJJ5m50beEzp/YOAxceATlPEYtaCiDg=="
  """.trimIndent()

  init {

      describe("Authorization") {
        it("should parse given authorization header") {
          val authorization = Authorization.parse(sampleAuthorization)
          authorization shouldBe Authorization(
            keyId = "MOCK_SUB_ID|key1|ed25519",
            created = 1627447288,
            expires = 1627450888,
            signature = "hrEaAq8fpChrKBuevWTRu1dl2evXcoDjuaSPCg+olGXqnR3NA7NyqqQIJJ5m50beEzp/YOAxceATlPEYtaCiDg=="
          )
        }

        it("should generate valid header string"){
          val authorization = Authorization.parse(sampleAuthorization)
          authorization?.headerString shouldBe sampleAuthorization
        }
      }
  }

}
