package org.beckn.one.sandbox.bap.message.repositories

import com.mongodb.client.MongoDatabase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.longs.shouldBeExactly
import org.beckn.one.sandbox.bap.configurations.TestDatabaseConfiguration
import org.beckn.one.sandbox.bap.message.entities.CategoryDao
import org.beckn.one.sandbox.bap.message.entities.DescriptorDao
import org.litote.kmongo.eq
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestDatabaseConfiguration::class])
class GenericRepositorySpec constructor(
  val database: MongoDatabase
) : DescribeSpec() {
  init {
    describe("Generic Repository") {

      context("for category") {
        val categoryRepository = GenericRepository.create<CategoryDao>(database)
        val furniture = CategoryDao(
          id = "category/c1",
          descriptor = DescriptorDao(
            name = "Furniture",
            longDesc = "Wooden, Iron, Plastic furniture for home"
          )
        )
        val livingRoomFurniture = CategoryDao(
          id = "category/c2",
          parentCategoryId = "category/c1",
          descriptor = DescriptorDao(
            name = "Furniture",
            longDesc = "Wooden, Iron, Plastic furniture for home"
          )
        )

        it("should save categories to collection") {
          categoryRepository.clear()
          categoryRepository.insertMany(listOf(furniture, livingRoomFurniture))
          categoryRepository.size() shouldBeExactly 2
        }

        it("should fetch all categories from collection") {
          categoryRepository.clear()
          categoryRepository.insertMany(listOf(furniture, livingRoomFurniture))
          categoryRepository.all().size shouldBeExactly 2
        }

        it("should filter all categories from collection based on query") {
          categoryRepository.clear()
          categoryRepository.insertMany(listOf(furniture, livingRoomFurniture))
          categoryRepository.findAll(CategoryDao::id eq furniture.id).size shouldBeExactly 1
        }
      }
    }
  }
}
