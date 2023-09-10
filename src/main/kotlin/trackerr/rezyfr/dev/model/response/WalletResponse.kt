package trackerr.rezyfr.dev.model.response

import kotlinx.serialization.Serializable

@Serializable
data class WalletResponse(
    val id: Int,
    val name: String,
    val balance: Long,
    val icon: String,
)
