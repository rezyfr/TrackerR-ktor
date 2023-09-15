package trackerr.rezyfr.dev.model.response.transaction

import kotlinx.serialization.Serializable

@Serializable
data class SummaryResponse(
    val totalIncome: Long,
    val totalExpense: Long,
)
