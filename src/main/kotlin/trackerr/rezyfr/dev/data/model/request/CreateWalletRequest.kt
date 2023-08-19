package trackerr.rezyfr.dev.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateWalletRequest(
    val name: String,
    val balance: Long,
    val color: Long,
    val icon: String,
)

// Example in json
