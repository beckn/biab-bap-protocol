package org.beckn.one.sandbox.bap.message.mappers

import org.beckn.one.sandbox.bap.message.entities.*
import org.beckn.protocol.schemas.*
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

interface GenericResponseMapper<Protocol : ProtocolResponse, Entity : BecknResponseDao> {
  fun entityToProtocol(entity: Entity): Protocol
  fun protocolToEntity(schema: Protocol): Entity
}

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.WARN,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = [DateMapper::class]
)
interface OnSearchResponseMapper : GenericResponseMapper<ProtocolOnSearch, OnSearchDao> {
  override fun entityToProtocol(entity: OnSearchDao): ProtocolOnSearch
  override fun protocolToEntity(schema: ProtocolOnSearch): OnSearchDao
}


@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.WARN,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = [DateMapper::class]
)
interface OnSelectResponseMapper : GenericResponseMapper<ProtocolOnSelect, OnSelectDao> {
  override fun entityToProtocol(entity: OnSelectDao): ProtocolOnSelect
  override fun protocolToEntity(schema: ProtocolOnSelect): OnSelectDao
}

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.WARN,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = [DateMapper::class]
)
interface OnInitResponseMapper : GenericResponseMapper<ProtocolOnInit, OnInitDao> {
  override fun entityToProtocol(entity: OnInitDao): ProtocolOnInit
  override fun protocolToEntity(schema: ProtocolOnInit): OnInitDao
}

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.WARN,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = [DateMapper::class]
)
interface OnConfirmResponseMapper : GenericResponseMapper<ProtocolOnConfirm, OnConfirmDao> {
  override fun entityToProtocol(entity: OnConfirmDao): ProtocolOnConfirm
  override fun protocolToEntity(schema: ProtocolOnConfirm): OnConfirmDao
}

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.WARN,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = [DateMapper::class]
)
interface OnTrackResponseMapper : GenericResponseMapper<ProtocolOnTrack, OnTrackDao> {
  override fun entityToProtocol(entity: OnTrackDao): ProtocolOnTrack
  override fun protocolToEntity(schema: ProtocolOnTrack): OnTrackDao
}

@Component
class DateMapper {
  fun map(instant: Instant?): OffsetDateTime? {
    return instant?.let { OffsetDateTime.ofInstant(it, ZoneId.of("UTC")) }
  }

  fun map(offset: OffsetDateTime?): Instant? {
    return offset?.toInstant()
  }
}
