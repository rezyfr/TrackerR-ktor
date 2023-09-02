package trackerr.rezyfr.dev.model.request

import kotlinx.serialization.Serializable
import trackerr.rezyfr.dev.model.response.IconType

@Serializable
data class AddIconRequest(
    val url: String,
    val type: IconType,
)
