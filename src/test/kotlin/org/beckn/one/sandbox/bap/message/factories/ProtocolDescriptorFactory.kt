package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.DescriptorDao
import org.beckn.protocol.schemas.ProtocolDescriptor

object ProtocolDescriptorFactory {

  fun create(type: String, id: String) = ProtocolDescriptor(
    name = "$type-$id name",
    code = "$type-$id code",
    symbol = "$type-$id symbol",
    shortDesc = "A short description about $type-$id",
    longDesc = "A long description about $type-$id",
    images = listOf("uri:https://$type-$id-image-1.com", "uri:https://$type-$id-image-2.com"),
    audio = "$type-$id-image-audio-file-path",
    threeDRender = "$type-$id-3d"
  )

  fun createAsEntity(protocol: ProtocolDescriptor?) = protocol?.let {
    DescriptorDao(
      name = protocol.name,
      code = protocol.code,
      symbol = protocol.symbol,
      shortDesc = protocol.shortDesc,
      longDesc = protocol.longDesc,
      images = protocol.images,
      audio = protocol.audio,
      threeDRender = protocol.threeDRender
    )
  }


}