package trackerr.rezyfr.dev.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class WalletResponse(
    val id: Int,
    val name: String,
    val balance: Long,
    val color: Long,
    val icon: String,
)
