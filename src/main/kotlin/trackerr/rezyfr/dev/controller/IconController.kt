package trackerr.rezyfr.dev.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import trackerr.rezyfr.dev.service.IconService

interface IconController {
    suspend fun getIconByType(call: ApplicationCall)
}

class IconControllerImpl(
    private val iconService: IconService
) : IconController {
    override suspend fun getIconByType(call: ApplicationCall) {
        try {
            call.parameters["type"]?.let {
                iconService.getIconByType(it).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            } ?: throw IllegalArgumentException("Type is required")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e)
        }
    }
}