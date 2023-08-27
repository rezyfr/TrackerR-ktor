package trackerr.rezyfr.dev.service

import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.response.BaseResponse
import trackerr.rezyfr.dev.model.response.WalletResponse
import trackerr.rezyfr.dev.repository.WalletRepository

interface WalletService {
     fun addWallet(wallet: Wallet) : BaseResponse<WalletResponse>
     fun getWalletByUserEmail(email: String) : BaseResponse<List<WalletResponse>>
     fun updateWalletBalance(walletId: Int, balance: Long, email: String) : BaseResponse<WalletResponse>
}

class WalletServiceImpl(
    private val walletRepository: WalletRepository,
) : WalletService {
    override fun addWallet(wallet: Wallet): BaseResponse<WalletResponse> {
        return BaseResponse(true, "Successfully added wallet", walletRepository.addWallet(wallet))
    }

    override fun getWalletByUserEmail(email: String): BaseResponse<List<WalletResponse>> {
        return BaseResponse(true, "Successfully retrieved wallet", walletRepository.findWalletByUserEmail(email))
    }

    override fun updateWalletBalance(walletId: Int, balance: Long, email: String): BaseResponse<WalletResponse> {
        walletRepository.findWalletById(walletId, email) ?: throw IllegalArgumentException("Wallet not found")
        return BaseResponse(true, "Successfully updated wallet", walletRepository.updateWalletBalance(walletId, balance))
    }
}