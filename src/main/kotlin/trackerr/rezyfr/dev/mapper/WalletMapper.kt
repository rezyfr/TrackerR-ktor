package trackerr.rezyfr.dev.mapper

import org.jetbrains.exposed.sql.ResultRow
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.model.response.WalletResponse

class WalletMapper {
    fun rowsToWallet(rows: List<ResultRow>?, icon: (Int) -> String): WalletResponse? {
        return rows?.firstOrNull()?.let {
            WalletResponse(
                id = it[WalletTable.id],
                name = it[WalletTable.name],
                balance = it[WalletTable.balance],
                icon = icon.invoke(it[WalletTable.icon])
            )
        }
    }

    fun rowToWallet(row: ResultRow, icon: (Int) -> String): WalletResponse {
        return WalletResponse(
            id = row[WalletTable.id],
            name = row[WalletTable.name],
            balance = row[WalletTable.balance],
            icon = icon.invoke(row[WalletTable.icon])
        )
    }
}