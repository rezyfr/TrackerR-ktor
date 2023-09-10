package trackerr.rezyfr.dev.model


data class Transaction(
    val amount: Double,
    val description: String,
    val categoryId: Int,
    val walletId: Int,
    val createdDate: String,
)
