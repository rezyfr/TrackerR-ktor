package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.*
import trackerr.rezyfr.dev.data.model.Category
import trackerr.rezyfr.dev.data.model.CategoryType
import trackerr.rezyfr.dev.data.model.response.CategoryResponse
import trackerr.rezyfr.dev.data.table.CategoryTable
import trackerr.rezyfr.dev.repository.DatabaseFactory.dbQuery

interface CategoryRepository {
    suspend fun addCategory(category: Category): CategoryResponse
    suspend fun getCategories(userEmail: String, type: CategoryType): List<CategoryResponse>
    suspend fun populateStarterCategories(userEmail: String)
}

class CategoryRepositoryImpl : CategoryRepository {
    override suspend fun addCategory(category: Category): CategoryResponse {
        return dbQuery {
            val rows = CategoryTable.insert {
                it[name] = category.name
                it[type] = category.type
                it[userEmail] = category.userEmail
            }.resultedValues
            rowsToCategory(rows)!!
        }
    }

    override suspend fun getCategories(userEmail: String, type: CategoryType): List<CategoryResponse> {
        return dbQuery {
            CategoryTable.select {
                CategoryTable.userEmail.eq(userEmail) and CategoryTable.type.eq(type)
            }.map {
                rowToCategory(it)
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

    private fun rowsToCategory(rows: List<ResultRow>?): CategoryResponse? {
        return rows?.map {
            CategoryResponse(
                id = it[CategoryTable.id],
                name = it[CategoryTable.name],
                type = it[CategoryTable.type]
            )
        }?.firstOrNull()
    }

    private fun rowToCategory(row: ResultRow): CategoryResponse {
        return row.let {
            CategoryResponse(
                id = it[CategoryTable.id],
                name = it[CategoryTable.name],
                type = it[CategoryTable.type]
            )
        }
    }
}
