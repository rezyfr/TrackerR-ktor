package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.db.table.IconTable
import trackerr.rezyfr.dev.mapper.CategoryMapper
import trackerr.rezyfr.dev.mapper.IconMapper

interface CategoryRepository {
     fun addCategory(category: Category): CategoryResponse
     fun getCategories(userEmail: String, type: CategoryType): List<CategoryResponse>
     fun populateStarterCategories(userEmail: String)
     fun getCategoryById(id: Int, userEmail: String): CategoryResponse?
}

class CategoryRepositoryImpl(
    private val mapper: CategoryMapper,
    private val iconMapper: IconMapper
) : CategoryRepository {
    override fun addCategory(category: Category): CategoryResponse {
        return transaction {
            val rows = CategoryTable.insert {
                it[name] = category.name
                it[type] = category.type.toString()
                it[userEmail] = category.userEmail
                it[iconId] = category.iconId
            }.resultedValues
            mapper.rowsToCategory(rows, icon = { getIconUrl(it) })!!
        }
    }

    override fun getCategories(userEmail: String, type: CategoryType): List<CategoryResponse> {
        return transaction {
            CategoryTable.select {
                CategoryTable.userEmail.eq(userEmail) and CategoryTable.type.eq(type.toString())
            }.map {
                mapper.rowToCategory(it, icon = { getIconUrl(it) })
            }
        }
    }

    override fun populateStarterCategories(userEmail: String) {
        transaction {
            CategoryTable.batchInsert(data = Category.getInitialCategories(userEmail), shouldReturnGeneratedValues = false) {
                this[CategoryTable.name] = it.name
                this[CategoryTable.type] = it.type.toString()
                this[CategoryTable.userEmail] = it.userEmail
                this[CategoryTable.iconId] = it.iconId
            }
        }
    }

    override fun getCategoryById(id: Int, userEmail: String): CategoryResponse? {
        return transaction {
            CategoryTable.select {
                CategoryTable.id.eq(id) and CategoryTable.userEmail.eq(userEmail)
            }.map {
                mapper.rowToCategory(it, icon = { getIconUrl(it) })
            }.firstOrNull()
        }
    }

    private fun getIconUrl(id: Int): String {
        return IconTable.select { IconTable.id.eq(id) }.first().let { icon ->
            iconMapper.rowToIcon(icon).url
        }
    }
}
