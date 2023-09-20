package trackerr.rezyfr.dev.model.response.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionReportResponse(
    val totalAmount: Long,
    val expense: TransactionReport,
    val income: TransactionReport
)

@Serializable
data class TransactionReport(
    val categoryId: Int?,
    val categoryAmount: Long?,
    val totalAmount: Long?
)