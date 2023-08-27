package trackerr.rezyfr.dev.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.db.table.TransactionTable
import trackerr.rezyfr.dev.db.table.UserTable
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.mapper.CategoryMapper
import trackerr.rezyfr.dev.mapper.TransactionMapper
import trackerr.rezyfr.dev.mapper.WalletMapper
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.Transaction
import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.CategoryResponse
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionRepositoryTest : BaseRepositoryTest() {

    lateinit var transactionRepository: TransactionRepository
    lateinit var userRepository: UserRepository
    lateinit var walletRepository: WalletRepository
    lateinit var categoryRepository: CategoryRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        transactionRepository = TransactionRepositoryImpl(TransactionMapper())
        userRepository = UserRepositoryImpl()
        walletRepository = WalletRepositoryImpl(WalletMapper())
        categoryRepository = CategoryRepositoryImpl(CategoryMapper())
    }

    @Test
    fun `should add transaction`() {
        withTables(UserTable, WalletTable, CategoryTable, TransactionTable) {
            // given
            userRepository.addUser(user)
            val wallet = Wallet("test", 1000L, user.email, 0xFFFFFF, "icon")

            // when
            val walletResponse = walletRepository.addWallet(wallet)
            val category = categoryRepository.addCategory(Category("test", user.email, CategoryType.INCOME))
            val transaction = transactionRepository.addTransaction(
                Transaction(BigDecimal(100), "test", category.id, walletResponse.id, "2021-01-01 00:00:00"),
                category,
                walletResponse,
                user.email,
            )

            // then
            assert(transaction.amount == 100F)
            assert(transaction.category == "test")
            assert(transaction.type == "INCOME")
            assert(transaction.wallet == "test")
        }
    }

    @Test
    fun `test recent transaction`() {

        withTables(UserTable, WalletTable, CategoryTable, TransactionTable) {
            // given
            userRepository.addUser(user)

            assert(transactionRepository.getRecentTransaction(user.email).isEmpty())

            val wallet = Wallet("test", 1000L, user.email, 0xFFFFFF, "icon")
            val walletResponse = walletRepository.addWallet(wallet)
            val category = categoryRepository.addCategory(Category("test", user.email, CategoryType.INCOME))
            repeat(5) {
                transactionRepository.addTransaction(
                    Transaction(BigDecimal(100), "test", category.id, walletResponse.id, "2021-01-01 00:00:0$it"),
                    category,
                    walletResponse,
                    user.email,
                )
            }

            val result = transactionRepository.getRecentTransaction(user.email)
            assert(result.size == 3)
            assert(result[0].createdDate == "2021-01-01T00:00:04")
            assert(result[1].createdDate == "2021-01-01T00:00:03")
            assert(result[2].createdDate == "2021-01-01T00:00:02")
        }
    }
}