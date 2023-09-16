package trackerr.rezyfr.dev.model.response.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionWithDateResponse(
    val date: String,
    val transactions: List<TransactionResponse>
)
