package org.beckn.one.sandbox.bap.message.factories

import org.beckn.one.sandbox.bap.message.entities.OnTrackMessageTrackingDao
import org.beckn.one.sandbox.bap.message.entities.OnTrackMessageTrackingDao.TrackingStatusDao
import org.beckn.protocol.schemas.ProtocolOnTrackMessageTracking
import org.beckn.protocol.schemas.ProtocolOnTrackMessageTracking.ProtocolTrackingStatus.Active
import org.beckn.protocol.schemas.ProtocolOnTrackMessageTracking.ProtocolTrackingStatus.Inactive

object ProtocolOnTrackMessageTrackingFactory {

  fun create(index: Int = 1): ProtocolOnTrackMessageTracking {
    return ProtocolOnTrackMessageTracking(
      url = "www.tracking-url-$index.com", status = Active
    )
  }

  fun createAsEntity(protocol: ProtocolOnTrackMessageTracking?) = protocol?.let {
    OnTrackMessageTrackingDao(
      url = it.url, status = when (it.status) {
        Active -> TrackingStatusDao.Active
        Inactive -> TrackingStatusDao.Inactive
        else -> null
      }
    )
  }

}