package trackerr.rezyfr.dev.service

import trackerr.rezyfr.dev.data.model.Transaction
import trackerr.rezyfr.dev.data.model.response.BaseResponse
import trackerr.rezyfr.dev.data.model.response.TransactionResponse
import trackerr.rezyfr.dev.repository.CategoryRepository
import trackerr.rezyfr.dev.repository.TransactionRepository
import trackerr.rezyfr.dev.repository.WalletRepository

interface TransactionService {
    suspend fun addTransaction(transaction: Transaction, email: String) : BaseResponse<TransactionResponse>
    suspend fun getRecentTransactions(email: String) : BaseResponse<List<TransactionResponse>>
}

class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
    private val categoryRepository: CategoryRepository
) : TransactionService {
    override suspend fun addTransaction(
        transaction: Transaction,
        email: String
    ) : BaseResponse<TransactionResponse> {
        val wallet = walletRepository.findWalletById(transaction.walletId, email) ?: throw Exception("Wallet not found")
        val category = categoryRepository.getCategoryById(transaction.categoryId, email) ?: throw Exception("Category not found")
        return BaseResponse(true, "Successfully added transaction", transactionRepository.addTransaction(transaction, category, wallet, email))
    }

    override suspend fun getRecentTransactions(email: String) : BaseResponse<List<TransactionResponse>> {
        return BaseResponse(true, "Successfully retrieved recent transactions", transactionRepository.getRecentTransaction(email))
    }
}