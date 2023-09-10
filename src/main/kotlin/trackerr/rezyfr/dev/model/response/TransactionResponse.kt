package trackerr.rezyfr.dev.model.response

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val id: Int,
    val amount: Float,
    val description: String,
    val category: CategoryResponse? = null,
    val categoryName: String,
    val type: String,
    val wallet: String,
    val walletIcon: String,
    val createdDate: String,
)
