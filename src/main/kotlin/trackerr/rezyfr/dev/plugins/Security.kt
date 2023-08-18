package trackerr.rezyfr.dev.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.kodein.di.instance
import trackerr.rezyfr.dev.authentication.JwtService
import trackerr.rezyfr.dev.controller.UserController

fun Application.configureSecurity() {
    val userController by ModulesConfig.kodein.instance<UserController>()
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    authentication {
        jwt {
            verifier(JwtService.verifier)
            realm = "trackerr.rezyfr.dev"
            validate { credential ->
                if (credential.payload.audience.contains(JwtService.audience)) {
                    val payload = credential.payload
                    val email = payload.getClaim("email").asString()
                    val user = userController.findUserByEmail(email)
                    user
                } else null
            }
        }
    }
    routing {
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
