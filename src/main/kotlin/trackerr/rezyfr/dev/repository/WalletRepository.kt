package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.mapper.WalletMapper
import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.WalletResponse

interface WalletRepository {
     fun addWallet(wallet: Wallet): WalletResponse
     fun findWalletByUserEmail(userEmail: String): List<WalletResponse>?
     fun findWalletById(id: Int, userEmail: String): WalletResponse?
     fun updateWalletBalance(id: Int, balance: Long): WalletResponse
}

class WalletRepositoryImpl(
    private val mapper: WalletMapper
) : WalletRepository {
    override fun addWallet(wallet: Wallet): WalletResponse {
        return transaction {
            val rows = WalletTable.insert {
                it[name] = wallet.name
                it[balance] = wallet.balance
                it[userEmail] = wallet.userEmail
                it[color] = wallet.color
                it[icon] = wallet.icon
            }.resultedValues
            mapper.rowsToWallet(rows)!!
        }
    }

    override fun findWalletByUserEmail(userEmail: String): List<WalletResponse>? {
        return transaction {
            WalletTable.select {
                WalletTable.userEmail.eq(userEmail)
            }.map(mapper::rowToWallet)
        }
    }

    override fun findWalletById(id: Int, userEmail: String): WalletResponse? {
        return transaction {
            WalletTable.select {
                WalletTable.id.eq(id)
                WalletTable.userEmail.eq(userEmail)
            }.map (mapper::rowToWallet)
        }.firstOrNull()
    }

    override fun updateWalletBalance(id: Int, balance: Long): WalletResponse {
        return transaction {
            WalletTable.update {
                it[WalletTable.balance] = balance
                it[WalletTable.id] = id
            }

            WalletTable
                .select { WalletTable.id.eq(id) }
                .map (mapper::rowToWallet)
                .first()
        }
    }
}