package org.beckn.one.sandbox.bap.message.mappers

import org.beckn.one.sandbox.bap.message.entities.ContextDao
import org.beckn.protocol.schemas.ProtocolContext
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
interface ContextMapper {
  fun toSchema(entity: ContextDao): ProtocolContext
  fun fromSchema(schema: ProtocolContext): ContextDao
}