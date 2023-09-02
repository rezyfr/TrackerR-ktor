package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import trackerr.rezyfr.dev.db.table.IconTable
import trackerr.rezyfr.dev.mapper.IconMapper
import trackerr.rezyfr.dev.model.Icon
import trackerr.rezyfr.dev.model.response.IconResponse

interface IconRepository {
    fun getIconByType(type: String): List<IconResponse>
}

class IconRepositoryImpl(
    private val mapper: IconMapper
) : IconRepository {
    override fun getIconByType(type: String): List<IconResponse> {
        return transaction {
            IconTable.select {
                IconTable.type.eq(type)
            }.map {
                mapper.rowToIcon(it)
            }
        }
    }
}