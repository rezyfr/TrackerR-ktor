package trackerr.rezyfr.dev.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.BaseResponse
import trackerr.rezyfr.dev.model.response.WalletResponse
import trackerr.rezyfr.dev.repository.WalletRepository

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class WalletServiceTest {

    private lateinit var walletService: WalletService
    private val wallletRepository: WalletRepository = mockk()

    @BeforeEach
    fun setUp() {
        walletService = WalletServiceImpl(wallletRepository)
    }

    @Test
    fun `test add wallet`() {
        val wallet = Wallet("test", 10000, "mail@mail.com", 0xffffff, "icon")

        coEvery { wallletRepository.addWallet(any()) } returns WalletResponse(
            1,
            wallet.name,
            wallet.balance,
            wallet.color,
            wallet.icon
        )

        val result = walletService.addWallet(wallet)

        coVerify { wallletRepository.addWallet(wallet) }
        assert(result.success == true)
        assert(result.data != null)
        assert(result.data!!.name == wallet.name)
        assert(result.data!!.balance == wallet.balance)
        assert(result.data!!.color == wallet.color)
        assert(result.data!!.icon == wallet.icon)
    }

    @Test
    fun `test get wallet by user email`() {
        val email= "mail@mail.com"
        val wallet = Wallet("test", 10000, email, 0xffffff, "icon")
        coEvery { wallletRepository.findWalletByUserEmail(email) } returns listOf(
            WalletResponse(
                1,
                wallet.name,
                wallet.balance,
                wallet.color,
                wallet.icon
            )
        )

        val result = walletService.getWalletByUserEmail(email)

        coVerify { wallletRepository.findWalletByUserEmail(email) }

        assert(result.success == true)
        assert(result.data != null)
        assert(result.data!!.isNotEmpty())
        assert(result.data!![0].name == wallet.name)
        assert(result.data!![0].balance == wallet.balance)
        assert(result.data!![0].color == wallet.color)
        assert(result.data!![0].icon == wallet.icon)
    }

    @Test
    fun `test update wallet balance`() {
        val email = "mail@mail.com"
        val wallet = Wallet("test", 10000, email, 0xffffff, "icon")
        coEvery { wallletRepository.findWalletById(1, email) } returns WalletResponse(
            1,
            wallet.name,
            wallet.balance,
            wallet.color,
            wallet.icon
        )
        coEvery { wallletRepository.updateWalletBalance(1, 20000) } returns WalletResponse(
            1,
            wallet.name,
            20000,
            wallet.color,
            wallet.icon
        )

        val result = walletService.updateWalletBalance(1, 20000, email)

        coVerify { wallletRepository.findWalletById(1, email) }
        coVerify { wallletRepository.updateWalletBalance(1, 20000) }

        assert(result.success == true)
        assert(result.data != null)
        assert(result.data!!.name == wallet.name)
        assert(result.data!!.balance == 20000L)
        assert(result.data!!.color == wallet.color)
        assert(result.data!!.icon == wallet.icon)
    }

    @Test
    fun `test failed update wallet balance`() {
        val email = "mail@mail.com"

        coEvery { wallletRepository.findWalletById(1, email) } returns null

        assertThrows<IllegalArgumentException> {
            walletService.updateWalletBalance(1, 20000, email)
        }

        coVerify { wallletRepository.findWalletById(1, email) }
        coVerify(exactly = 0) { wallletRepository.updateWalletBalance(1, 20000) }

    }
}