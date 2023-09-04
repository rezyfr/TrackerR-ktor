package trackerr.rezyfr.dev.db.table

import org.jetbrains.exposed.sql.Table

object CategoryTable : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val type = varchar("type", 7)
    val userEmail = varchar("user_email", 128).references(UserTable.email)
    val iconId = integer("icon_id").references(IconTable.id)

    override val primaryKey = PrimaryKey(id)
}