package org.beckn.one.sandbox.bap.client.shared.security

import org.bouncycastle.crypto.Signer
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import java.util.*

object Cryptic{

  fun verify(
    signature: String,
    b64PublicKey: String,
    requestData: String
  ): Boolean {
    val signer = getEd25519SignerForVerification(b64PublicKey)
    signer.update(requestData.toByteArray(), 0, requestData.length)
    return signer.verifySignature(Base64.getDecoder().decode(signature))
  }

  private fun getEd25519SignerForVerification(b64PublicKey: String): Signer {
    val publicKey = Base64.getDecoder().decode(b64PublicKey)
    val cipherParams = Ed25519PublicKeyParameters(publicKey, 0)
    val sv = Ed25519Signer()
    sv.init(false, cipherParams)
    return sv
  }
}




