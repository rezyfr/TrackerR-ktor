package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import trackerr.rezyfr.dev.db.table.IconTable
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.mapper.IconMapper
import trackerr.rezyfr.dev.mapper.WalletMapper
import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.WalletResponse

interface WalletRepository {
    fun addWallet(wallet: Wallet): WalletResponse
    fun findWalletByUserEmail(userEmail: String): List<WalletResponse>?
    fun findWalletById(id: Int, userEmail: String): WalletResponse?
    fun updateWalletBalance(id: Int, balance: Long): WalletResponse
    fun getWalletBalance(email: String): Long
}

class WalletRepositoryImpl(
    private val mapper: WalletMapper,
    private val iconMapper: IconMapper
) : WalletRepository {
    override fun addWallet(wallet: Wallet): WalletResponse {
        return transaction {
            val rows = WalletTable.insert {
                it[name] = wallet.name
                it[balance] = wallet.balance
                it[userEmail] = wallet.userEmail
                it[icon] = wallet.iconId
            }.resultedValues

            mapper.rowsToWallet(rows, icon = { getIconUrl(it) })!!
        }
    }

    override fun findWalletByUserEmail(userEmail: String): List<WalletResponse>? {
        return transaction {
            WalletTable.select {
                WalletTable.userEmail.eq(userEmail)
            }.map {
                mapper.rowToWallet(it, icon = { getIconUrl(it) })
            }
        }
    }

    override fun findWalletById(id: Int, userEmail: String): WalletResponse? {
        return transaction {
            WalletTable.select {
                WalletTable.id.eq(id) and WalletTable.userEmail.eq(userEmail)
            }.map {
                mapper.rowToWallet(it, icon = { getIconUrl(it) })
            }
        }.firstOrNull()
    }

    override fun updateWalletBalance(id: Int, balance: Long): WalletResponse {
        return transaction {
            WalletTable.update(
                where = { WalletTable.id.eq(id) },
                body = { it[WalletTable.balance] = balance }
            )

            WalletTable
                .select { WalletTable.id.eq(id) }
                .map {
                    mapper.rowToWallet(it, icon = { getIconUrl(it) })
                }
                .first()
        }
    }

    override fun getWalletBalance(email: String): Long {
        return transaction {
            WalletTable.select { WalletTable.userEmail.eq(email) }
                .map { it[WalletTable.balance] }
                .sum()
        }
    }

    private fun getIconUrl(id: Int): String {
        return IconTable.select { IconTable.id.eq(id) }.first().let { icon ->
            iconMapper.rowToIcon(icon).url
        }
    }
}