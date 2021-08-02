package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.OnRatingMessageFeedbackDao
import org.beckn.protocol.schemas.ProtocolOnRatingMessageFeedback

object ProtocolOnRatingMessageFeedbackFactory {

  fun create(index: Int = 1): ProtocolOnRatingMessageFeedback {
    return ProtocolOnRatingMessageFeedback(
      id = "item id $index", descriptor = "item id  $index descriptor", parentId = "item id  $index parent id  $index"
    )
  }

  fun createAsEntity(protocol: ProtocolOnRatingMessageFeedback?) = protocol?.let {
    OnRatingMessageFeedbackDao(protocol.id, protocol.descriptor, protocol.parentId)
  }

}