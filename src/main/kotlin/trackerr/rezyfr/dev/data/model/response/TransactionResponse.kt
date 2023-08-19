package trackerr.rezyfr.dev.data.model.response

import kotlinx.serialization.Serializable
import trackerr.rezyfr.dev.data.model.Wallet

@Serializable
data class TransactionResponse(
    val id: Int,
    val amount: Float,
    val description: String,
    val category: String,
    val type: String,
    val wallet: String,
    val createdDate: String,
)
