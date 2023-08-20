package trackerr.rezyfr.dev.util

import io.ktor.server.application.*
import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class PasswordManager(
    application: Application
) {

    private val hashConfig = application.environment.config.config("hash")
    private val hashKey = hashConfig.property("hash_key").getString().toByteArray()
    private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

    fun hash(password: String): String {
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(hmacKey)
        return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
    }
}