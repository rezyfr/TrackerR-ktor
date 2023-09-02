package trackerr.rezyfr.dev.mapper

import org.jetbrains.exposed.sql.ResultRow
import trackerr.rezyfr.dev.db.table.IconTable
import trackerr.rezyfr.dev.model.response.IconResponse

class IconMapper {
    fun rowToIcon(row: ResultRow): IconResponse {
        return IconResponse(
            id = row[IconTable.id],
            url = row[IconTable.url],
        )
    }
}