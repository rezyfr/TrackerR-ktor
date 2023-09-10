package trackerr.rezyfr.dev.repository

import io.ktor.server.util.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.db.table.IconTable
import trackerr.rezyfr.dev.db.table.TransactionTable
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.mapper.IconMapper
import trackerr.rezyfr.dev.mapper.TransactionMapper
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.Transaction
import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.model.response.SummaryResponse
import trackerr.rezyfr.dev.model.response.TransactionResponse
import trackerr.rezyfr.dev.model.response.WalletResponse
import trackerr.rezyfr.dev.util.getEndOfMonth
import trackerr.rezyfr.dev.util.getStartOfMonth
import java.math.BigDecimal

interface TransactionRepository {
    fun addTransaction(
        transaction: Transaction,
        category: CategoryResponse,
        wallet: WalletResponse,
        email: String
    ): TransactionResponse

    fun getRecentTransaction(email: String): List<TransactionResponse>
    fun getMonthlySummary(month: Int, email: String): SummaryResponse
}

class TransactionRepositoryImpl(
    private val mapper: TransactionMapper,
    private val iconMapper: IconMapper
) : TransactionRepository {
    override fun addTransaction(
        transaction: Transaction,
        category: CategoryResponse,
        wallet: WalletResponse,
        email: String
    ): TransactionResponse {
        return transaction {
            val rows = TransactionTable.insert {
                it[amount] = BigDecimal(transaction.amount)
                it[desc] = transaction.description
                it[categoryId] = transaction.categoryId
                it[type] = category.type.toString()
                it[walletId] = transaction.walletId
                it[userEmail] = email
            }.resultedValues

            if (rows != null) {
                WalletTable.update(
                    where = { WalletTable.id.eq(wallet.id) },
                    body = {
                        val amount = if (category.type == CategoryType.EXPENSE) {
                            transaction.amount * -1
                        } else {
                            transaction.amount
                        }
                        it[balance] = wallet.balance + amount.toLong()
                    }
                )
            }
            mapper.rowsToTransaction(rows, category, wallet)!!
        }
    }

    override fun getRecentTransaction(email: String): List<TransactionResponse> {
        return transaction {
            TransactionTable.select { TransactionTable.userEmail.eq(email) }
                .orderBy(TransactionTable.id to SortOrder.DESC).limit(3).map { trxRow ->
                val cat = CategoryTable.select { CategoryTable.id.eq(trxRow[TransactionTable.categoryId]) }.first()
                    .let { catRow ->
                        CategoryResponse(
                            id = catRow[CategoryTable.id],
                            name = catRow[CategoryTable.name],
                            type = CategoryType.valueOf(catRow[CategoryTable.type]),
                            icon = getIconUrl(catRow[CategoryTable.iconId]),
                            color = catRow[CategoryTable.color]
                        )
                    }
                val wallet = WalletTable.select { WalletTable.id.eq(trxRow[TransactionTable.walletId]) }.first()
                    .let { walletRow ->
                        Wallet(
                            name = walletRow[WalletTable.name],
                            balance = walletRow[WalletTable.balance],
                            userEmail = walletRow[WalletTable.userEmail],
                            iconId = walletRow[WalletTable.icon]
                        )
                    }
                mapper.rowToTransaction(trxRow, cat, wallet, icon = { getIconUrl(it) })
            }
        }
    }

    @OptIn(InternalAPI::class)
    override fun getMonthlySummary(month: Int, email: String): SummaryResponse {
        return transaction {
            val startDate =
                getStartOfMonth(month).toLocalDateTime()
            val endDate = getEndOfMonth(month).toLocalDateTime()
            val income = TransactionTable.select {
                TransactionTable.userEmail.eq(email) and
                TransactionTable.date.between(startDate, endDate) and
                TransactionTable.type.eq(CategoryType.INCOME.toString())
            }.map {
                it[TransactionTable.amount].toLong()
            }.sum()
            val expense = TransactionTable.select {
                TransactionTable.userEmail.eq(email) and
                TransactionTable.date.between(startDate, endDate) and
                TransactionTable.type.eq(CategoryType.EXPENSE.toString())
            }.map {
                it[TransactionTable.amount].toLong()
            }.sum()
            SummaryResponse(income, expense)
        }
    }


    private fun getIconUrl(id: Int): String {
        return IconTable.select { IconTable.id.eq(id) }.first().let { icon ->
            iconMapper.rowToIcon(icon).url
        }
    }
}
