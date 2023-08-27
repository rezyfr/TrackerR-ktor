package trackerr.rezyfr.dev.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import trackerr.rezyfr.dev.db.table.UserTable
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.model.Wallet

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WalletRepositoryTest : BaseRepositoryTest() {

        lateinit var walletRepository: WalletRepository
        lateinit var userRepository: UserRepository

        @BeforeEach
        override fun setUp() {
            super.setUp()
            walletRepository = WalletRepositoryImpl()
            userRepository = UserRepositoryImpl()
        }

        @Test
        fun `test add wallet`() {
            withTables(WalletTable, UserTable) {
                userRepository.addUser(user)
                val wallet = Wallet("test", 1000, user.email, 0xfffffff, "icon")
                walletRepository.addWallet(wallet).let {
                    val result = walletRepository.findWalletById(it.id, wallet.userEmail)
                    assert(result != null)
                    assert(result!!.name == wallet.name)
                    assert(result.balance == wallet.balance)
                    assert(result.color == wallet.color)
                    assert(result.icon == wallet.icon)
                }
            }
        }
}