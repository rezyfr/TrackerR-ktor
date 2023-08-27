package trackerr.rezyfr.dev.mapper

import org.jetbrains.exposed.sql.ResultRow
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.model.response.WalletResponse

class WalletMapper {
    fun rowsToWallet(rows: List<ResultRow>?): WalletResponse? {
        return rows?.firstOrNull()?.let {
            WalletResponse(
                id = it[WalletTable.id],
                name = it[WalletTable.name],
                balance = it[WalletTable.balance],
                color = it[WalletTable.color],
                icon = it[WalletTable.icon]
            )
        }
    }

    fun rowToWallet(row: ResultRow): WalletResponse {
        return WalletResponse(
            id = row[WalletTable.id],
            name = row[WalletTable.name],
            balance = row[WalletTable.balance],
            color = row[WalletTable.color],
            icon = row[WalletTable.icon]
        )
    }
}