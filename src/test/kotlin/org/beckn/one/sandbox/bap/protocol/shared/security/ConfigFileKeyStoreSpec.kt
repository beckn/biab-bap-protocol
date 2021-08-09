package org.beckn.one.sandbox.bap.protocol.shared.security

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(value = ["test"])
class ConfigFileKeyStoreSpec @Autowired constructor(
  configFileKeyStore: ConfigFileKeyStore
): DescribeSpec() {

  init {
    describe("ConfigFileKeyStoreSpec") {
      it("should find and return public key for given id") {
        val b64PublicKey = configFileKeyStore.getBase64PublicKey("MOCK_SUB_ID", "key1")
        b64PublicKey shouldBe "h5W9FOm5C3POe7wQShwi+Uw7C8bMO5qiRSNHMgu/2vA="
      }
    }
  }
}
