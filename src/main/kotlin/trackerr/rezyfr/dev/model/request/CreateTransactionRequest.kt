package trackerr.rezyfr.dev.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateTransactionRequest(
    val amount: Double,
    val description: String,
    val categoryId: Int,
    val walletId: Int,
    val createdDate: String
)
