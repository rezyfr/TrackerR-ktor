package trackerr.rezyfr.dev.service

import trackerr.rezyfr.dev.model.response.BaseResponse
import trackerr.rezyfr.dev.model.response.IconResponse
import trackerr.rezyfr.dev.repository.IconRepository

interface IconService {
    fun getIconByType(type: String): BaseResponse<List<IconResponse>>
}

class IconServiceImpl(
    private val iconRepository: IconRepository
) : IconService {
    override fun getIconByType(type: String): BaseResponse<List<IconResponse>> {
        return BaseResponse(true, "Successfully retrieved icon", iconRepository.getIconByType(type))
    }
}