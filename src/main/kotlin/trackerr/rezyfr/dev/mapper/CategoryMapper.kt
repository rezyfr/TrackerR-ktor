package trackerr.rezyfr.dev.mapper

import org.jetbrains.exposed.sql.ResultRow
import trackerr.rezyfr.dev.data.model.response.CategoryResponse
import trackerr.rezyfr.dev.data.table.CategoryTable

class CategoryMapper {
    fun rowsToCategory(rows: List<ResultRow>?): CategoryResponse? {
        return rows?.map {
            CategoryResponse(
                id = it[CategoryTable.id],
                name = it[CategoryTable.name],
                type = it[CategoryTable.type]
            )
        }?.firstOrNull()
    }

    fun rowToCategory(row: ResultRow): CategoryResponse {
        return row.let {
            CategoryResponse(
                id = it[CategoryTable.id],
                name = it[CategoryTable.name],
                type = it[CategoryTable.type]
            )
        }
    }
}