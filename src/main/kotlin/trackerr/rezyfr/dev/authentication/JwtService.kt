package trackerr.rezyfr.dev.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import trackerr.rezyfr.dev.data.model.User

object JwtService {
    private val issuer = "TrackerrServer"
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)
    val audience = System.getenv("JWT_AUDIENCE")

    val verifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("email", user.email)
        .sign(algorithm)

}