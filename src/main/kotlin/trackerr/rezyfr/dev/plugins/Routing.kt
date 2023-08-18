package trackerr.rezyfr.dev.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import trackerr.rezyfr.dev.controller.UserController
import trackerr.rezyfr.dev.route.userRoutes

fun Application.configureRouting() {
    val userController by ModulesConfig.kodein.instance<UserController>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        userRoutes(userController)
    }
}
