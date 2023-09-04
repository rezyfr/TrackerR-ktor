package trackerr.rezyfr.dev.mapper

import org.jetbrains.exposed.sql.ResultRow
import trackerr.rezyfr.dev.db.table.TransactionTable
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.model.response.TransactionResponse
import trackerr.rezyfr.dev.model.response.WalletResponse

class TransactionMapper {
    fun rowsToTransaction(
        rows: Iterable<ResultRow>?,
        category: CategoryResponse,
        wallet: WalletResponse,
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
                walletIcon = wallet.icon,
                categoryIcon = category.icon
            )
        }?.firstOrNull()
    }

    fun rowToTransaction(
        row: ResultRow,
        category: Category,
        wallet: Wallet,
        icon: (Int) -> String
    ): TransactionResponse {
        return TransactionResponse(
            id = row[TransactionTable.id],
            amount = row[TransactionTable.amount].toFloat(),
            description = row[TransactionTable.desc],
            category = category.name,
            type = category.type.name,
            wallet = wallet.name,
            createdDate = row[TransactionTable.date].toString(),
            categoryIcon = icon(category.iconId),
            walletIcon = icon(wallet.iconId)
        )
    }
}