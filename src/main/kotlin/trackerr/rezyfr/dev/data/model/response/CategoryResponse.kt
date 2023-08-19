package trackerr.rezyfr.dev.data.model.response

import kotlinx.serialization.Serializable
import trackerr.rezyfr.dev.data.model.CategoryType

@Serializable
data class CategoryResponse(
    val id: Int,
    val name: String,
    val type: CategoryType
)
