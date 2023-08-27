package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.mapper.CategoryMapper

interface CategoryRepository {
     fun addCategory(category: Category): CategoryResponse
     fun getCategories(userEmail: String, type: CategoryType): List<CategoryResponse>
     fun populateStarterCategories(userEmail: String)
     fun getCategoryById(id: Int, userEmail: String): CategoryResponse?
}

class CategoryRepositoryImpl(
    private val mapper: CategoryMapper
) : CategoryRepository {
    override fun addCategory(category: Category): CategoryResponse {
        return transaction {
            val rows = CategoryTable.insert {
                it[name] = category.name
                it[type] = category.type
                it[userEmail] = category.userEmail
            }.resultedValues
            mapper.rowsToCategory(rows)!!
        }
    }

    override fun getCategories(userEmail: String, type: CategoryType): List<CategoryResponse> {
        return transaction {
            CategoryTable.select {
                CategoryTable.userEmail.eq(userEmail) and CategoryTable.type.eq(type)
            }.map {
                mapper.rowToCategory(it)
            }
        }
    }

    override fun populateStarterCategories(userEmail: String) {
        transaction {
            try {
                CategoryTable.batchInsert(data = Category.getInitialCategories(userEmail), shouldReturnGeneratedValues = false) {
                    this[CategoryTable.name] = it.name
                    this[CategoryTable.type] = it.type
                    this[CategoryTable.userEmail] = it.userEmail
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getCategoryById(id: Int, userEmail: String): CategoryResponse? {
        return transaction {
            CategoryTable.select {
                CategoryTable.id.eq(id)
                CategoryTable.userEmail.eq(userEmail)
            }.map {
                mapper.rowToCategory(it)
            }.firstOrNull()
        }
    }
}
