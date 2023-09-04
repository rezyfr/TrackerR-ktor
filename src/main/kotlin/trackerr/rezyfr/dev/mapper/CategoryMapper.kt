package trackerr.rezyfr.dev.mapper

import org.jetbrains.exposed.sql.ResultRow
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.model.CategoryType

class CategoryMapper {
    fun rowsToCategory(rows: List<ResultRow>?, icon: (Int) -> String): CategoryResponse? {
        return rows?.map {
            CategoryResponse(
                id = it[CategoryTable.id],
                name = it[CategoryTable.name],
                type = CategoryType.valueOf(it[CategoryTable.type]),
                icon = icon(it[CategoryTable.iconId])
            )
        }?.firstOrNull()
    }

    fun rowToCategory(row: ResultRow, icon: (Int) -> String): CategoryResponse {
        return row.let {
            CategoryResponse(
                id = it[CategoryTable.id],
                name = it[CategoryTable.name],
                type = CategoryType.valueOf(it[CategoryTable.type]),
                icon = icon(it[CategoryTable.iconId])
            )
        }
    }
}