package trackerr.rezyfr.dev.model.response

import kotlinx.serialization.SerialName

data class Icon(
    val type: IconType,
    val url: String
)

enum class IconType {
    @SerialName("wallet")
    WALLET,
    @SerialName("category")
    CATEGORY,
}