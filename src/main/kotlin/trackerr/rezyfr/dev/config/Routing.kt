package trackerr.rezyfr.dev.config

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import trackerr.rezyfr.dev.controller.CategoryController
import trackerr.rezyfr.dev.controller.TransactionController
import trackerr.rezyfr.dev.controller.UserController
import trackerr.rezyfr.dev.controller.WalletController
import trackerr.rezyfr.dev.route.categoryRoutes
import trackerr.rezyfr.dev.route.transactionRoutes
import trackerr.rezyfr.dev.route.userRoutes
import trackerr.rezyfr.dev.route.walletRoutes

fun Application.configureRouting(di: DI) {
    val userController: UserController by di.instance()
    val walletController by di.instance<WalletController>()
    val categoryController by di.instance<CategoryController>()
    val transactionController by di.instance<TransactionController>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        userRoutes(userController)
        walletRoutes(walletController)
        categoryRoutes(categoryController)
        transactionRoutes(transactionController)
    }
}
