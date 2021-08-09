package org.beckn.one.sandbox.bap.protocol.shared.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

interface CrypticKeyStore {
  fun getBase64PublicKey(subscriberId: String, keyId: String): String
}

@Component
class ConfigFileKeyStore @Autowired constructor(
  private val env: Environment
) : CrypticKeyStore {
  override fun getBase64PublicKey(subscriberId: String, keyId: String): String =
    env.getRequiredProperty("subscribers.public_keys.$subscriberId.$keyId")
}