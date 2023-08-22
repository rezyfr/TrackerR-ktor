package trackerr.rezyfr.dev.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String,
    val status: Boolean
)
