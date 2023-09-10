package trackerr.rezyfr.dev.model.request

import kotlinx.serialization.Serializable
import trackerr.rezyfr.dev.model.CategoryType

@Serializable
data class CreateCategoryRequest(
    val name: String,
    val type: CategoryType,
    val iconId: Int,
    val color: String
)
