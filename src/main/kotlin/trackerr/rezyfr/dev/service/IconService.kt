package trackerr.rezyfr.dev.service

import trackerr.rezyfr.dev.model.request.AddIconRequest
import trackerr.rezyfr.dev.model.response.BaseResponse
import trackerr.rezyfr.dev.model.response.Icon
import trackerr.rezyfr.dev.model.response.IconResponse
import trackerr.rezyfr.dev.model.response.IconType
import trackerr.rezyfr.dev.repository.IconRepository

interface IconService {
    fun getIconByType(type: IconType): BaseResponse<List<IconResponse>>
    fun addIcon(icon: Icon): BaseResponse<IconResponse>
}

class IconServiceImpl(
    private val iconRepository: IconRepository
) : IconService {
    override fun getIconByType(type: IconType): BaseResponse<List<IconResponse>> {
        return BaseResponse(true, "Successfully retrieved icon", iconRepository.getIconByType(type.name))
    }

    override fun addIcon(icon: Icon): BaseResponse<IconResponse> {
        return BaseResponse(true, "Successfully added icon", iconRepository.addIcon(icon))
    }
}