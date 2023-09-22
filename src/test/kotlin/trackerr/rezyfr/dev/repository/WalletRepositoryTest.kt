package trackerr.rezyfr.dev.repository

import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import trackerr.rezyfr.dev.db.table.UserTable
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.mapper.IconMapper
import trackerr.rezyfr.dev.mapper.WalletMapper
import trackerr.rezyfr.dev.model.Wallet

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WalletRepositoryTest : BaseRepositoryTest() {

    lateinit var walletRepository: WalletRepository
    lateinit var userRepository: UserRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        walletRepository = WalletRepositoryImpl(WalletMapper(), IconMapper())
        userRepository = UserRepositoryImpl()
    }

    @Test
    fun `test add wallet`() {
        withTables(WalletTable, UserTable) {
            userRepository.addUser(user)
            val wallet = Wallet("test", 1000, user.email,  1)

            val nullResult = walletRepository.findWalletById(0, user.email)
            assert(nullResult == null)

            walletRepository.addWallet(wallet).let {
                val result = walletRepository.findWalletById(it.id, wallet.userEmail)
                assert(result != null)
                assert(result!!.name == wallet.name)
                assert(result.balance == wallet.balance)
            }
        }
    }

    @Test
    fun `test find wallet by user email`() {
        withTables(WalletTable, UserTable) {
            userRepository.addUser(user)

            val emptyResult = walletRepository.findWalletByUserEmail(user.email)
            assert(emptyResult?.isEmpty() == true)

            val wallet = Wallet("test", 1000, user.email, 1)
            walletRepository.addWallet(wallet).let {
                val result = walletRepository.findWalletByUserEmail(wallet.userEmail)
                assert(result != null)
                assert(result!!.size == 1)
                assert(result[0].name == wallet.name)
                assert(result[0].balance == wallet.balance)
            }
        }
    }

    @Test
    fun `test update wallet balance`() {
        withTables(WalletTable, UserTable) {
            userRepository.addUser(user)

            val wallet = Wallet("test", 1000, user.email,  1)
            val newBalance = 2000L
            val newWallet = walletRepository.addWallet(wallet)

            val result = walletRepository.updateWalletBalance(newWallet.id, 2000)

            assert(result.balance == newBalance)
        }
    }
}