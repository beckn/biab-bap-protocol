package org.beckn.one.sandbox.bap.protocol.shared.security

interface CryptoKeyStore {
  fun getBase64PublicKey(subscriberId: String, keyId: String): String
}