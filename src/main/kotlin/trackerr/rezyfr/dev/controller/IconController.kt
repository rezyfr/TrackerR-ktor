package trackerr.rezyfr.dev.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import trackerr.rezyfr.dev.model.request.AddIconRequest
import trackerr.rezyfr.dev.model.response.ErrorResponse
import trackerr.rezyfr.dev.model.response.Icon
import trackerr.rezyfr.dev.model.response.IconType
import trackerr.rezyfr.dev.service.IconService

interface IconController {
    suspend fun getIconByType(call: ApplicationCall)
    suspend fun addIcon(call: ApplicationCall)
}

class IconControllerImpl(
    private val iconService: IconService
) : IconController {
    override suspend fun getIconByType(call: ApplicationCall) {
        try {
            call.parameters["type"]?.let {
                iconService.getIconByType(IconType.valueOf(it)).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            } ?: throw IllegalArgumentException("Type is required")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message.orEmpty(), false))
        }
    }

    override suspend fun addIcon(call: ApplicationCall) {
        try {
            call.receive<AddIconRequest>().let {
                iconService.addIcon(Icon(type = it.type, url = it.url)).let {
                    call.respond(HttpStatusCode.Created, it)
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message.orEmpty(), false))
        }
    }
}