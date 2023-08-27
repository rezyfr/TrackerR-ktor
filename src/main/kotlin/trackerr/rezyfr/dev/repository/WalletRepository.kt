package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.WalletResponse
import trackerr.rezyfr.dev.db.table.WalletTable

interface WalletRepository {
     fun addWallet(wallet: Wallet): WalletResponse
     fun findWalletByUserEmail(userEmail: String): List<WalletResponse>?
     fun findWalletById(id: Int, userEmail: String): WalletResponse?
     fun updateWalletBalance(id: Int, balance: Long): WalletResponse
}

class WalletRepositoryImpl : WalletRepository {
    override fun addWallet(wallet: Wallet): WalletResponse {
        return transaction {
            val rows = WalletTable.insert {
                it[name] = wallet.name
                it[balance] = wallet.balance
                it[userEmail] = wallet.userEmail
                it[color] = wallet.color
                it[icon] = wallet.icon
            }.resultedValues
            rowsToWallet(rows)!!
        }
    }

    override fun findWalletByUserEmail(userEmail: String): List<WalletResponse>? {
        return transaction {
            WalletTable.select {
                WalletTable.userEmail.eq(userEmail)
            }.map {
                rowToWallet(it)
            }
        }
    }

    override fun findWalletById(id: Int, userEmail: String): WalletResponse? {
        return transaction {
            WalletTable.select {
                WalletTable.id.eq(id)
                WalletTable.userEmail.eq(userEmail)
            }.map {
                rowToWallet(it)
            }.firstOrNull()
        }
    }

    override fun updateWalletBalance(id: Int, balance: Long): WalletResponse {
        return transaction {
            WalletTable.update {
                it[WalletTable.balance] = balance
                it[WalletTable.id] = id
            }

            WalletTable.select { WalletTable.id.eq(id) }.map {
                rowToWallet(it)
            }.first()
        }
    }

    private fun rowsToWallet(rows: List<ResultRow>?): WalletResponse? {
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

    private fun rowToWallet(row: ResultRow): WalletResponse {
        return WalletResponse(
            id = row[WalletTable.id],
            name = row[WalletTable.name],
            balance = row[WalletTable.balance],
            color = row[WalletTable.color],
            icon = row[WalletTable.icon]
        )
    }
}