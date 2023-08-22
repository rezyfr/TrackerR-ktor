package trackerr.rezyfr.dev.service

import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.response.BaseResponse
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.repository.CategoryRepository

interface CategoryService {
    suspend fun addCategory(category: Category) : BaseResponse<CategoryResponse>
    suspend fun getCategories(userEmail: String, type: CategoryType) : BaseResponse<List<CategoryResponse>>
}

class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {
    override suspend fun addCategory(category: Category) : BaseResponse<CategoryResponse> {
        return BaseResponse(true, "Successfully added category", categoryRepository.addCategory(category))
    }

    override suspend fun getCategories(userEmail: String, type: CategoryType) : BaseResponse<List<CategoryResponse>> {
        return BaseResponse(true, "Successfully retrieved categories", categoryRepository.getCategories(userEmail, type))
    }

}