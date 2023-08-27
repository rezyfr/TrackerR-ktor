package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.db.table.TransactionTable
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.mapper.TransactionMapper
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.Transaction
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.model.response.TransactionResponse
import trackerr.rezyfr.dev.model.response.WalletResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface TransactionRepository {
     fun addTransaction(
        transaction: Transaction,
        category: CategoryResponse,
        wallet: WalletResponse,
        email: String
    ): TransactionResponse

     fun getRecentTransaction(email: String): List<TransactionResponse>
}

class TransactionRepositoryImpl(
    private val mapper: TransactionMapper
) : TransactionRepository {
    override fun addTransaction(
        transaction: Transaction,
        category: CategoryResponse,
        wallet: WalletResponse,
        email: String
    ): TransactionResponse {
        return transaction {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val rows = TransactionTable.insert {
                it[amount] = transaction.amount
                it[desc] = transaction.description
                it[categoryId] = transaction.categoryId
                it[type] = category.type.toString()
                it[walletId] = transaction.walletId
                it[date] = LocalDateTime.parse(transaction.createdDate, formatter)
                it[userEmail] = email
            }.resultedValues
            mapper.rowsToTransaction(rows, category, wallet)!!
        }
    }

    override fun getRecentTransaction(email: String): List<TransactionResponse> {
        return transaction {
            TransactionTable.select { TransactionTable.userEmail.eq(email) }.orderBy(TransactionTable.id to SortOrder.DESC).limit(3).map { trxRow ->
                val cat = CategoryTable.select { CategoryTable.id.eq(trxRow[TransactionTable.categoryId]) }.first().let { catRow ->
                    Category(
                        name = catRow[CategoryTable.name],
                        type = CategoryType.valueOf(catRow[CategoryTable.type]),
                        userEmail = catRow[CategoryTable.userEmail]
                    )
                }
                val walletName = WalletTable.select { WalletTable.id.eq(trxRow[TransactionTable.walletId]) }.first().let { walletRow ->
                    walletRow[WalletTable.name]
                }
                mapper.rowToTransaction(trxRow, cat, walletName)
            }
        }
    }
}
