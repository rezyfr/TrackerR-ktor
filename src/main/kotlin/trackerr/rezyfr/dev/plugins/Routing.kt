package trackerr.rezyfr.dev.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import trackerr.rezyfr.dev.controller.CategoryController
import trackerr.rezyfr.dev.controller.UserController
import trackerr.rezyfr.dev.controller.WalletController
import trackerr.rezyfr.dev.route.categoryRoutes
import trackerr.rezyfr.dev.route.userRoutes
import trackerr.rezyfr.dev.route.walletRoutes

fun Application.configureRouting() {
    val userController by ModulesConfig.kodein.instance<UserController>()
    val walletController by ModulesConfig.kodein.instance<WalletController>()
    val categoryController by ModulesConfig.kodein.instance<CategoryController>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        userRoutes(userController)
        walletRoutes(walletController)
        categoryRoutes(categoryController)
    }
}
