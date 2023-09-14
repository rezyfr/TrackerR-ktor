package trackerr.rezyfr.dev.model.response

import kotlinx.serialization.Serializable
import trackerr.rezyfr.dev.model.CategoryType

@Serializable
data class CategoryResponse(
    val id: Int,
    val name: String,
    val type: CategoryType,
    val icon: String,
    val color: Long
)
