package trackerr.rezyfr.dev.data.model.request

import kotlinx.serialization.Serializable
import trackerr.rezyfr.dev.data.model.CategoryType

@Serializable
data class CreateCategoryRequest(
    val name: String,
    val type: CategoryType
)
