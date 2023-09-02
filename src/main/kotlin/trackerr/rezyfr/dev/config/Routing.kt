package trackerr.rezyfr.dev.config

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import trackerr.rezyfr.dev.controller.*
import trackerr.rezyfr.dev.route.*

fun Application.configureRouting(di: DI) {
    val userController: UserController by di.instance()
    val walletController by di.instance<WalletController>()
    val categoryController by di.instance<CategoryController>()
    val transactionController by di.instance<TransactionController>()
    val iconController by di.instance<IconController>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        userRoutes(userController)
        walletRoutes(walletController)
        categoryRoutes(categoryController)
        transactionRoutes(transactionController)
        iconRoutes(iconController)
    }
}
