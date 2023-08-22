package trackerr.rezyfr.dev.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateWalletBalanceRequest(
    val id: Int,
    val balance: Long,
)
