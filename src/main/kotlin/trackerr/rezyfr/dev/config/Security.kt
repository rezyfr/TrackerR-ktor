package trackerr.rezyfr.dev.config

import com.auth0.jwt.interfaces.JWTVerifier
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.kodein.di.DI
import org.kodein.di.instance
import trackerr.rezyfr.dev.util.JwtService
import trackerr.rezyfr.dev.controller.UserController
import trackerr.rezyfr.dev.model.response.ErrorResponse

fun Application.configureSecurity(di: DI) {

    val userController by di.instance<UserController>()
    val jwtService by di.instance<JwtService>()

    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    authentication {
        jwt {
            verifier(jwtService.verifier)
            realm = "trackerr.rezyfr.dev"

            challenge { _, _ ->
                // get custom error message if error exists
                val header = call.request.headers["Authorization"]
                header?.let {
                    if (it.isNotEmpty()) {
                        try {
                            if ((!it.contains("Bearer", true))) throw JWTDecodeException("")
                            val jwt = it.replace("Bearer ", "")
                            jwtService.verifier.verify(jwt)
                            ""
                        } catch (e: TokenExpiredException) {
                            call.respond(
                                HttpStatusCode.Unauthorized,
                                ErrorResponse("Authentication failed: Access token expired", false)
                            )
                        } catch (e: SignatureVerificationException) {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                ErrorResponse("Authentication failed: Failed to parse Access token", false)
                            )
                        } catch (e: JWTDecodeException) {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                ErrorResponse("Authentication failed: Failed to parse Access token", false)
                            )
                        }
                    } else call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("Authentication failed: Access token not found", false)
                    )
                } ?: call.respond(
                    HttpStatusCode.Unauthorized, ErrorResponse("Authentication failed: No authorization header found", false)
                )
                ErrorResponse("Unauthorized", false)
            }

            validate { credential ->
                if (credential.payload.audience.contains(jwtService.audience)) {
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
