package trackerr.rezyfr.dev.db.table

import org.jetbrains.exposed.sql.Table
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.util.PGEnum

object CategoryTable : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val type = customEnumeration("type", "CategoryType", { value -> CategoryType.valueOf(value as String) }, { PGEnum("CategoryType", it) })
    val userEmail = varchar("user_email", 128).references(UserTable.email)

    override val primaryKey = PrimaryKey(id)
}