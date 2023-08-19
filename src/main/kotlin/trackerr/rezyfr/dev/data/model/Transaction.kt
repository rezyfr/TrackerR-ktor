package trackerr.rezyfr.dev.data.model

import java.math.BigDecimal

data class Transaction(
    val amount: BigDecimal,
    val description: String,
    val categoryId: Int,
    val walletId: Int,
    val createdDate: String,
)
