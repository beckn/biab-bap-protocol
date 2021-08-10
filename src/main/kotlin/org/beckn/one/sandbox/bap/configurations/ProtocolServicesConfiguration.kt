package org.beckn.one.sandbox.bap.configurations

import org.beckn.one.sandbox.bap.message.entities.*
import org.beckn.one.sandbox.bap.message.mappers.GenericResponseMapper
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.services.MessageService
import org.beckn.one.sandbox.bap.message.services.ResponseStorageService
import org.beckn.one.sandbox.bap.message.services.ResponseStorageServiceImpl
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.protocol.schemas.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProtocolServicesConfiguration {

  @Bean
  fun onSearchStorageService(
    @Autowired responseRepo: BecknResponseRepository<OnSearchDao>,
    @Autowired mapper: GenericResponseMapper<ProtocolOnSearch, OnSearchDao>
  ): ResponseStorageService<ProtocolOnSearch> = ResponseStorageServiceImpl(responseRepo, mapper)

  @Bean
  fun onSelectStorageService(
    @Autowired responseRepo: BecknResponseRepository<OnSelectDao>,
    @Autowired mapper: GenericResponseMapper<ProtocolOnSelect, OnSelectDao>
  ): ResponseStorageService<ProtocolOnSelect> = ResponseStorageServiceImpl(responseRepo, mapper)


  @Bean
  fun onInitStorageService(
    @Autowired responseRepo: BecknResponseRepository<OnInitDao>,
    @Autowired mapper: GenericResponseMapper<ProtocolOnInit, OnInitDao>
  ): ResponseStorageService<ProtocolOnInit> = ResponseStorageServiceImpl(responseRepo, mapper)

  @Bean
  fun onConfirmStorageService(
    @Autowired responseRepo: BecknResponseRepository<OnConfirmDao>,
    @Autowired mapper: GenericResponseMapper<ProtocolOnConfirm, OnConfirmDao>
  ): ResponseStorageService<ProtocolOnConfirm> = ResponseStorageServiceImpl(responseRepo, mapper)

  @Bean
  fun onTrackStorageService(
    @Autowired responseRepo: BecknResponseRepository<OnTrackDao>,
    @Autowired mapper: GenericResponseMapper<ProtocolOnTrack, OnTrackDao>,
  ): ResponseStorageService<ProtocolOnTrack> = ResponseStorageServiceImpl(responseRepo, mapper)

  @Bean
  fun onSupportStorageService(
    @Autowired responseRepo: BecknResponseRepository<OnSupportDao>,
    @Autowired mapper: GenericResponseMapper<ProtocolOnSupport, OnSupportDao>
  ): ResponseStorageService<ProtocolOnSupport> = ResponseStorageServiceImpl(responseRepo, mapper)

  @Bean
  fun onRatingStorageService(
    @Autowired responseRepo: BecknResponseRepository<OnRatingDao>,
    @Autowired mapper: GenericResponseMapper<ProtocolOnRating, OnRatingDao>,
  ): ResponseStorageService<ProtocolOnRating> = ResponseStorageServiceImpl(responseRepo, mapper)

  @Bean
  fun onCancelStorageService(
    @Autowired responseRepo: BecknResponseRepository<OnCancelDao>,
    @Autowired mapper: GenericResponseMapper<ProtocolOnCancel, OnCancelDao>
  ): ResponseStorageService<ProtocolOnCancel> = ResponseStorageServiceImpl(responseRepo, mapper)

  @Bean
  fun onOrderStatusStorageService(
    @Autowired responseRepo: BecknResponseRepository<OnOrderStatusDao>,
    @Autowired mapper: GenericResponseMapper<ProtocolOnOrderStatus, OnOrderStatusDao>,
  ): ResponseStorageService<ProtocolOnOrderStatus> = ResponseStorageServiceImpl(responseRepo, mapper)

  @Bean
  fun pollForSearchResponseService(
    messageService: MessageService,
    responseStorageService: ResponseStorageService<ProtocolOnSearch>
  ) = PollForResponseService(messageService, responseStorageService)

  @Bean
  fun pollForSelectResponseService(
    messageService: MessageService,
    responseStorageService: ResponseStorageService<ProtocolOnSelect>
  ) = PollForResponseService(messageService, responseStorageService)

  @Bean
  fun pollForInitResponseService(
    messageService: MessageService,
    responseStorageService: ResponseStorageService<ProtocolOnInit>
  ) = PollForResponseService(messageService, responseStorageService)

  @Bean
  fun pollForConfirmResponseService(
    messageService: MessageService,
    responseStorageService: ResponseStorageService<ProtocolOnConfirm>
  ) = PollForResponseService(messageService, responseStorageService)

  @Bean
  fun pollForTrackResponseService(
    messageService: MessageService,
    responseStorageService: ResponseStorageService<ProtocolOnTrack>
  ) = PollForResponseService(messageService, responseStorageService)

  @Bean
  fun pollForSupportResponseService(
    messageService: MessageService,
    responseStorageService: ResponseStorageService<ProtocolOnSupport>
  ) = PollForResponseService(messageService, responseStorageService)

  @Bean
  fun pollForRatingResponseService(
    messageService: MessageService,
    responseStorageService: ResponseStorageService<ProtocolOnRating>
  ) = PollForResponseService(messageService, responseStorageService)

  @Bean
  fun pollForCancelResponseService(
    messageService: MessageService,
    responseStorageService: ResponseStorageService<ProtocolOnCancel>
  ) = PollForResponseService(messageService, responseStorageService)

  @Bean
  fun pollForOrderStatusResponseService(
    messageService: MessageService,
    responseStorageService: ResponseStorageService<ProtocolOnOrderStatus>
  ) = PollForResponseService(messageService, responseStorageService)
}
