package trackerr.rezyfr.dev

import io.ktor.server.application.*
import io.ktor.server.locations.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import trackerr.rezyfr.dev.db.configureDatabase
import trackerr.rezyfr.dev.config.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
fun Application.module() {
    kodeinApplication {
        install(Locations)

        configureDatabase()

        configureSerialization()
    }
}

fun Application.kodeinApplication(kodeinMapper: DI.MainBuilder.(Application) -> Unit) {
    val app = this
    val kodein = DI {
        bindSingleton<Application> { app }
        import(authModule)
        import(mapperModule)
        import(userModule)
        import(walletModule)
        import(categoryModule)
        import(transactionModule)
        import(iconModule)
        kodeinMapper(this, app)
    }
    configureSecurity(kodein)
    configureRouting(kodein)
}