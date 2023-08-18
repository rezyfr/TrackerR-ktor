package trackerr.rezyfr.dev.data.table

import org.jetbrains.exposed.sql.Table

object WalletTable : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val balance = long("balance")
    val userEmail = varchar("user_email", 128).references( UserTable.email)
    val color = long("color")
    val icon = varchar("icon", 128)

    override val primaryKey = PrimaryKey(id)
}