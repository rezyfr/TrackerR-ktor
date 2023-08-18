package trackerr.rezyfr.dev.data.model.response

import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse<T> (
    val success: Boolean? = null,
    val message: String? = null,
    val data: T? = null
)