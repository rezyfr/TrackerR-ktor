package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.*
import trackerr.rezyfr.dev.data.model.Category
import trackerr.rezyfr.dev.data.model.CategoryType
import trackerr.rezyfr.dev.data.model.response.CategoryResponse
import trackerr.rezyfr.dev.data.table.CategoryTable
import trackerr.rezyfr.dev.mapper.CategoryMapper
import trackerr.rezyfr.dev.db.DatabaseFactory.dbQuery

interface CategoryRepository {
    suspend fun addCategory(category: Category): CategoryResponse
    suspend fun getCategories(userEmail: String, type: CategoryType): List<CategoryResponse>
    suspend fun populateStarterCategories(userEmail: String)
    suspend fun getCategoryById(id: Int, userEmail: String): CategoryResponse?
}

class CategoryRepositoryImpl(
    private val mapper: CategoryMapper
) : CategoryRepository {
    override suspend fun addCategory(category: Category): CategoryResponse {
        return dbQuery {
            val rows = CategoryTable.insert {
                it[name] = category.name
                it[type] = category.type
                it[userEmail] = category.userEmail
            }.resultedValues
            mapper.rowsToCategory(rows)!!
        }
    }

    override suspend fun getCategories(userEmail: String, type: CategoryType): List<CategoryResponse> {
        return dbQuery {
            CategoryTable.select {
                CategoryTable.userEmail.eq(userEmail) and CategoryTable.type.eq(type)
            }.map {
                mapper.rowToCategory(it)
            }
        }
    }

    override suspend fun populateStarterCategories(userEmail: String) {
        dbQuery {
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

    override suspend fun getCategoryById(id: Int, userEmail: String): CategoryResponse? {
        return dbQuery {
            CategoryTable.select {
                CategoryTable.id.eq(id)
                CategoryTable.userEmail.eq(userEmail)
            }.map {
                mapper.rowToCategory(it)
            }.firstOrNull()
        }
    }
}
