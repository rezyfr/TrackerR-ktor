package trackerr.rezyfr.dev.model.response.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionFrequencyResponse(
    val index: Int,
    val incomeAmount: Long,
    val expenseAmount: Long,
)
