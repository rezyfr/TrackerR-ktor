package trackerr.rezyfr.dev

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.locations.*
import io.ktor.server.netty.*
import trackerr.rezyfr.dev.plugins.configureRouting
import trackerr.rezyfr.dev.plugins.configureSecurity
import trackerr.rezyfr.dev.plugins.configureSerialization
import trackerr.rezyfr.dev.db.configureDatabase

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
}

fun Application.module() {
    install(Locations)

    configureDatabase()

    configureSecurity()
    configureSerialization()
    configureRouting()

}
