package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import trackerr.rezyfr.dev.db.table.IconTable
import trackerr.rezyfr.dev.mapper.IconMapper
import trackerr.rezyfr.dev.model.request.AddIconRequest
import trackerr.rezyfr.dev.model.response.Icon
import trackerr.rezyfr.dev.model.response.IconResponse

interface IconRepository {
    fun getIconByType(type: String): List<IconResponse>
    fun addIcon(icon: Icon): IconResponse
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

    override fun addIcon(icon: Icon): IconResponse {
        return transaction {
            IconTable.insert {
                it[type] = icon.type.name
                it[url] = icon.url
            }.resultedValues!!.let {
                mapper.rowToIcon(it.first())
            }
        }
    }
}