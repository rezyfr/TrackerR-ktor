package trackerr.rezyfr.dev.route

import io.ktor.server.locations.*
import io.ktor.server.routing.*
import trackerr.rezyfr.dev.controller.IconController

const val GET_ICON = "$API_VERSION/icon"

@Location(GET_ICON)
class GetIconRoute

fun Route.iconRoutes(
    iconController: IconController
) {
    get<GetIconRoute> { iconController.getIconByType(context) }
}