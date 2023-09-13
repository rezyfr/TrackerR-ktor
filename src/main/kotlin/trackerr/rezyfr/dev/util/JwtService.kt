package trackerr.rezyfr.dev.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import trackerr.rezyfr.dev.model.User
import trackerr.rezyfr.dev.model.response.TokenResponse
import java.util.*

class JwtService(
    application: Application
) {

    private val issuer = "TrackerrServer"
    private val jwtConfig = application.environment.config.config("jwt")
    private val jwtSecret = jwtConfig.property("secret").getString()
    private val algorithm = Algorithm.HMAC512(jwtSecret)
    val audience = jwtConfig.property("audience").getString()
    private val tokenExpiry = 60000L * 60L * 24L
    private val refreshTokenExpiry = tokenExpiry * 7L

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun createToken(user: User): TokenResponse {
        return TokenResponse(
            generateAccessToken(user),
            generateRefreshToken(user)
        )
    }

    fun generateAccessToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("email", user.email)
        .withClaim("type", "access_token")
        .withExpiresAt(Date(System.currentTimeMillis() + tokenExpiry))
        .sign(algorithm)

    fun generateRefreshToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("email", user.email)
        .withClaim("type", "refresh_token")
        .withExpiresAt(Date(System.currentTimeMillis() + refreshTokenExpiry))
        .sign(algorithm)

    fun isTokenExpired(token: String): Boolean {
        return verifier.verify(token).expiresAt.before(Date(System.currentTimeMillis()))
    }

    fun getTokenType(token: String): String {
        return verifier.verify(token)
            .claims["type"]!!
            .asString()
    }
}