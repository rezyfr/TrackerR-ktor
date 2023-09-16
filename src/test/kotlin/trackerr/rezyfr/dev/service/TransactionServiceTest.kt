package trackerr.rezyfr.dev.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.Transaction
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.model.response.transaction.TransactionResponse
import trackerr.rezyfr.dev.model.response.WalletResponse
import trackerr.rezyfr.dev.repository.CategoryRepository
import trackerr.rezyfr.dev.repository.TransactionRepository
import trackerr.rezyfr.dev.repository.WalletRepository

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TransactionServiceTest {

    private lateinit var transactionService: TransactionService
    private val transactionRepository: TransactionRepository = mockk()
    private val walletRepository: WalletRepository = mockk()
    private val categoryRepository: CategoryRepository = mockk()

    @BeforeEach
    fun setUp() {
        transactionService = TransactionServiceImpl(transactionRepository, walletRepository, categoryRepository)
    }

    @Test
    fun `test add transaction`() {
        coEvery { walletRepository.findWalletById(any(), any()) } returns WalletResponse(1, "test", 10000, 0xffffff, "icon")
        coEvery { categoryRepository.getCategoryById(any(), any()) } returns CategoryResponse(1, "test", CategoryType.EXPENSE, "icon")
        coEvery { transactionRepository.addTransaction(any(), any(), any(), any()) } returns TransactionResponse(1, 10000f, "desc", "cat", CategoryType.EXPENSE.name, "wallet", "", "", "2021-01-01T00:00:00.000Z")

        val transaction = Transaction(10000.0, "desc", 1, 1, "2021-01-01T00:00:00.000Z")
        val email = "mail@mail.com"

        val result = transactionService.addTransaction(transaction, email)

        coVerify(exactly = 1) { walletRepository.findWalletById(any(), any()) }
        coVerify(exactly = 1) { categoryRepository.getCategoryById(any(), any()) }
        coVerify(exactly = 1) { transactionRepository.addTransaction(any(), any(), any(), any()) }

        assert(result.status == true)
        assert(result.data != null)
        assert(result.data!!.amount == transaction.amount.toFloat())
        assert(result.data!!.description == transaction.description)
    }

    @Test
    fun `test add transaction with no wallet found`() {
        coEvery { walletRepository.findWalletById(any(), any()) } returns null

        val transaction = Transaction(10000.0, "desc", 1, 1, "2021-01-01T00:00:00.000Z")

        assertThrows<Exception> {
            transactionService.addTransaction(transaction, "mail@mail.com")
        }
    }

    @Test
    fun `test add transaction with no category found`() {
        coEvery { walletRepository.findWalletById(any(), any()) } returns null
        coEvery { categoryRepository.getCategoryById(any(), any()) } returns null

        val transaction = Transaction(10000.0, "desc", 1, 1, "2021-01-01T00:00:00.000Z")

        assertThrows<Exception> {
            transactionService.addTransaction(transaction, "mail@mail.com")
        }
    }

    @Test
    fun `test get recent transactions`() {
        val email = "mail@mail.com"
        coEvery { transactionRepository.getRecentTransaction(email) } returns listOf(
            TransactionResponse(
                1,
                10000f,
                "desc",
                "cat",
                CategoryType.EXPENSE.name,
                "wallet",
                "","",
                "2021-01-01T00:00:00.000Z"
            )
        )

        val result = transactionService.getRecentTransactions(email)

        coVerify(exactly = 1) { transactionRepository.getRecentTransaction(email) }

        assert(result.status == true)
        assert(result.data != null)
        assert(result.data!!.isNotEmpty())
        assert(result.data!![0].amount == 10000f)
        assert(result.data!![0].description == "desc")
        assert(result.data!![0].category == "cat")
        assert(result.data!![0].type == CategoryType.EXPENSE.name)
        assert(result.data!![0].wallet == "wallet")
        assert(result.data!![0].createdDate == "2021-01-01T00:00:00.000Z")
    }
}