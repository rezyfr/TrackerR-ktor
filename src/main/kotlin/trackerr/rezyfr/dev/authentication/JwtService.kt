package trackerr.rezyfr.dev.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import trackerr.rezyfr.dev.data.model.User

class JwtService(
    application: Application
) {

    private val issuer = "TrackerrServer"
    private val jwtConfig = application.environment.config.config("jwt")
    private val jwtSecret = jwtConfig.property("secret").getString()
    private val algorithm = Algorithm.HMAC512(jwtSecret)
    val audience = jwtConfig.property("audience").getString()

    val verifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User, application: Application): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("email", user.email)
        .sign(algorithm)

}