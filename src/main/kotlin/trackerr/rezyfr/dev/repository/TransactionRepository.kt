package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.select
import trackerr.rezyfr.dev.data.model.Category
import trackerr.rezyfr.dev.data.model.Transaction
import trackerr.rezyfr.dev.data.model.Wallet
import trackerr.rezyfr.dev.data.model.response.CategoryResponse
import trackerr.rezyfr.dev.data.model.response.TransactionResponse
import trackerr.rezyfr.dev.data.model.response.WalletResponse
import trackerr.rezyfr.dev.data.table.CategoryTable
import trackerr.rezyfr.dev.data.table.TransactionTable
import trackerr.rezyfr.dev.data.table.WalletTable
import trackerr.rezyfr.dev.repository.DatabaseFactory.dbQuery
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface TransactionRepository {
    suspend fun addTransaction(
        transaction: Transaction,
        category: CategoryResponse,
        wallet: WalletResponse,
        email: String
    ): TransactionResponse

    suspend fun getRecentTransaction(email: String): List<TransactionResponse>
}

class TransactionRepositoryImpl : TransactionRepository {
    override suspend fun addTransaction(
        transaction: Transaction,
        category: CategoryResponse,
        wallet: WalletResponse,
        email: String
    ): TransactionResponse {
        return dbQuery {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val rows = TransactionTable.insert {
                it[amount] = transaction.amount
                it[desc] = transaction.description
                it[categoryId] = transaction.categoryId
                it[type] = category.type
                it[walletId] = transaction.walletId
                it[date] = LocalDateTime.parse(transaction.createdDate, formatter)
                it[userEmail] = email
            }.resultedValues
            rowsToTransaction(rows, category, wallet)!!
        }
    }

    override suspend fun getRecentTransaction(email: String): List<TransactionResponse> {
        return dbQuery {
            TransactionTable.select { TransactionTable.userEmail.eq(email) }.orderBy(TransactionTable.id to SortOrder.DESC).limit(3).map { trxRow ->
                val cat = CategoryTable.select { CategoryTable.id.eq(trxRow[TransactionTable.categoryId]) }.first().let { catRow ->
                    Category(
                        name = catRow[CategoryTable.name],
                        type = catRow[CategoryTable.type],
                        userEmail = catRow[CategoryTable.userEmail]
                    )
                }
                val walletName = WalletTable.select { WalletTable.id.eq(trxRow[TransactionTable.walletId]) }.first().let { walletRow ->
                    walletRow[WalletTable.name]
                }
                rowToTransaction(trxRow, cat, walletName)
            }
        }
    }

    private fun rowsToTransaction(
        rows: Iterable<ResultRow>?,
        category: CategoryResponse,
        wallet: WalletResponse
    ): TransactionResponse? {
        return rows?.map {
            TransactionResponse(
                id = it[TransactionTable.id],
                amount = it[TransactionTable.amount].toFloat(),
                description = it[TransactionTable.desc],
                category = category.name,
                type = category.type.name,
                wallet = wallet.name,
                createdDate = it[TransactionTable.date].toString(),
            )
        }?.firstOrNull()
    }

    private fun rowToTransaction(row: ResultRow, category: Category, wallet: String): TransactionResponse {
        return TransactionResponse(
            id = row[TransactionTable.id],
            amount = row[TransactionTable.amount].toFloat(),
            description = row[TransactionTable.desc],
            category = category.name,
            type = category.type.name,
            wallet = wallet,
            createdDate = row[TransactionTable.date].toString(),
        )
    }
}
