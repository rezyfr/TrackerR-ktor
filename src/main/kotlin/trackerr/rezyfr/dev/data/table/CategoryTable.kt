package trackerr.rezyfr.dev.data.table

import org.jetbrains.exposed.sql.Table
import trackerr.rezyfr.dev.data.model.CategoryType

object CategoryTable : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val type = enumeration("type", CategoryType::class)
    val userEmail = varchar("user_email", 128).references( UserTable.email)

    override val primaryKey = PrimaryKey(id)
}