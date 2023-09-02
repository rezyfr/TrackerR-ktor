package trackerr.rezyfr.dev.db.table

import org.jetbrains.exposed.sql.Table


object IconTable : Table() {
    val id = integer("id").autoIncrement()
    val url = varchar("url", 255)
    val type = varchar("type", 128)

    override val primaryKey = PrimaryKey(id)
}