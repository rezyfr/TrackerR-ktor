package trackerr.rezyfr.dev.model.response

import kotlinx.serialization.Serializable

@Serializable
data class IconResponse(
    val id: Int,
    val url: String,
)
