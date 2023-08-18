package trackerr.rezyfr.dev.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateWalletBalanceRequest(
    val id: Int,
    val balance: Long,
)
