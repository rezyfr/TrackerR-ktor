package trackerr.rezyfr.dev.service

import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.BaseResponse
import trackerr.rezyfr.dev.model.response.WalletResponse
import trackerr.rezyfr.dev.repository.WalletRepository

interface WalletService {
    suspend fun addWallet(wallet: Wallet) : BaseResponse<WalletResponse>
    suspend fun getWalletByUserEmail(email: String) : BaseResponse<List<WalletResponse>>
    suspend fun updateWalletBalance(walletId: Int, balance: Long, email: String) : BaseResponse<WalletResponse>
}

class WalletServiceImpl(
    private val walletRepository: WalletRepository,
) : WalletService {
    override suspend fun addWallet(wallet: Wallet): BaseResponse<WalletResponse> {
        return BaseResponse(true, "Successfully added wallet", walletRepository.addWallet(wallet))
    }

    override suspend fun getWalletByUserEmail(email: String): BaseResponse<List<WalletResponse>> {
        return BaseResponse(true, "Successfully retrieved wallet", walletRepository.findWalletByUserEmail(email))
    }

    override suspend fun updateWalletBalance(walletId: Int, balance: Long, email: String): BaseResponse<WalletResponse> {
        walletRepository.findWalletById(walletId, email) ?: throw IllegalArgumentException("Wallet not found")
        return BaseResponse(true, "Successfully updated wallet", walletRepository.updateWalletBalance(walletId, balance))
    }
}