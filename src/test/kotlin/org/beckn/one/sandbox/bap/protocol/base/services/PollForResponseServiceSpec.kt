package org.beckn.one.sandbox.bap.protocol.base.services

import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import org.beckn.one.sandbox.bap.message.entities.MessageDao
import org.beckn.one.sandbox.bap.message.entities.OnSearchDao
import org.beckn.one.sandbox.bap.message.entities.OnSearchMessageDao
import org.beckn.one.sandbox.bap.message.factories.ProtocolCatalogFactory
import org.beckn.one.sandbox.bap.message.factories.ProtocolContextFactory
import org.beckn.one.sandbox.bap.message.repositories.BecknResponseRepository
import org.beckn.one.sandbox.bap.message.repositories.GenericRepository
import org.beckn.one.sandbox.bap.protocol.shared.services.PollForResponseService
import org.beckn.protocol.schemas.ProtocolOnSearch
import org.beckn.protocol.schemas.ProtocolOnSearchMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles(value = ["test"])
@TestPropertySource(locations = ["/application-test.yml"])
internal class PollForResponseServiceSpec @Autowired constructor(
  private val responseService: PollForResponseService<ProtocolOnSearch>,
  private val searchResultRepo: BecknResponseRepository<OnSearchDao>,
  private val messageRepository: GenericRepository<MessageDao>
) : DescribeSpec() {

  private val context = ProtocolContextFactory.fixed
  private val entityContext = ProtocolContextFactory.fixedAsEntity(context)
  val catalog = ProtocolCatalogFactory.create(1)

  init {
    describe("BaseResponseService") {
      searchResultRepo.clear()
      messageRepository.insertOne(MessageDao(id = entityContext.messageId, type = MessageDao.Type.Search))
      searchResultRepo.insertMany(entitySearchResults())

      it("should return search results for given message id") {
        val response = responseService.findResponses(entityContext.messageId)
        val searchResponse = ProtocolOnSearch(context = context, message = ProtocolOnSearchMessage(catalog))
        response.shouldBeRight(listOf(searchResponse, searchResponse))
      }
    }
  }

  fun entitySearchResults(): List<OnSearchDao> {
    val entitySearchResponse = OnSearchDao(
      context = entityContext,
      message = OnSearchMessageDao(ProtocolCatalogFactory.createAsEntity(catalog))
    )
    return listOf(
      entitySearchResponse,
      entitySearchResponse,
      entitySearchResponse.copy(context = entityContext.copy(messageId = "123"))
    )
  }

}