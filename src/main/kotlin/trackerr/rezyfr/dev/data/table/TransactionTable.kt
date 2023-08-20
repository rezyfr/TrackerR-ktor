package trackerr.rezyfr.dev.data.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import trackerr.rezyfr.dev.data.model.CategoryType
import trackerr.rezyfr.dev.util.PGEnum
import java.time.LocalDateTime

object TransactionTable : Table() {
    val id = integer("id").autoIncrement()
    val amount = decimal("amount", 12, 2)
    val desc = varchar("desc", 512)
    val date = datetime("created_at").clientDefault { LocalDateTime.now() }
    val walletId = integer("wallet_id").references(WalletTable.id)
    val categoryId = integer("category_id").references(CategoryTable.id)
    val type = customEnumeration("type", "CategoryType", { value -> CategoryType.valueOf(value as String) }, { PGEnum("CategoryType", it) })
    val userEmail = varchar("user_email", 128).references(UserTable.email)

    override val primaryKey = PrimaryKey(id)
}