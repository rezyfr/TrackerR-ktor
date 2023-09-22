package trackerr.rezyfr.dev.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.db.table.TransactionTable
import trackerr.rezyfr.dev.db.table.UserTable
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.mapper.CategoryMapper
import trackerr.rezyfr.dev.mapper.IconMapper
import trackerr.rezyfr.dev.mapper.TransactionMapper
import trackerr.rezyfr.dev.mapper.WalletMapper
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.Transaction
import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.model.response.Icon
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionRepositoryTest : BaseRepositoryTest() {

    lateinit var transactionRepository: TransactionRepository
    lateinit var userRepository: UserRepository
    lateinit var walletRepository: WalletRepository
    lateinit var categoryRepository: CategoryRepository
    lateinit var iconRepository: IconRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        transactionRepository = TransactionRepositoryImpl(TransactionMapper(), IconMapper())
        userRepository = UserRepositoryImpl()
        walletRepository = WalletRepositoryImpl(WalletMapper(), IconMapper())
        categoryRepository = CategoryRepositoryImpl(CategoryMapper(), IconMapper())
        iconRepository = IconRepositoryImpl(IconMapper())
    }

    @Test
    fun `should add transaction`() {
        withTables(UserTable, WalletTable, CategoryTable, TransactionTable) {
            // given
            userRepository.addUser(user)
            val wallet = Wallet("test", 1000L, user.email, 1)

            // when
            val walletResponse = walletRepository.addWallet(wallet)
            val category = categoryRepository.addCategory(Category("test", user.email, CategoryType.INCOME, 1,0xffffffff))
            val transaction = transactionRepository.addTransaction(
                Transaction(100.0, "test", category.id, walletResponse.id, "2021-01-01 00:00:00"),
                category,
                walletResponse,
                user.email,
            )

            // then
            assert(transaction.amount == 100F)
            assert(transaction.type == "INCOME")
            assert(transaction.wallet == "test")
        }
    }

    @Test
    fun `test recent transaction`() {

        withTables(UserTable, WalletTable, CategoryTable, TransactionTable) {
            // given
            userRepository.addUser(user)
            iconRepository.addIcon(icon)

            assert(transactionRepository.getRecentTransaction(user.email).isEmpty())

            val wallet = Wallet("test", 1000L, user.email,  1)
            val walletResponse = walletRepository.addWallet(wallet)
            val category = categoryRepository.addCategory(Category("test", user.email, CategoryType.INCOME, 1, 0xffffffff))
            repeat(5) {
                transactionRepository.addTransaction(
                    Transaction(100.0, "test", category.id, walletResponse.id, "2021-01-01T00:00:0$it.000000"),
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