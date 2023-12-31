package trackerr.rezyfr.dev.db.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.util.PGEnum
import java.time.LocalDateTime

object TransactionTable : Table() {
    val id = integer("id").autoIncrement()
    val amount = decimal("amount", 12, 2)
    val desc = varchar("desc", 512)
    val date = datetime("created_at").clientDefault { LocalDateTime.now() }
    val walletId = integer("wallet_id").references(WalletTable.id)
    val categoryId = integer("category_id").references(CategoryTable.id)
    val type = varchar("type", 7)
    val userEmail = varchar("user_email", 128).references(UserTable.email)

    override val primaryKey = PrimaryKey(id)
}