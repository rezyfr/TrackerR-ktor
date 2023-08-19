package trackerr.rezyfr.dev.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import trackerr.rezyfr.dev.data.model.User
import trackerr.rezyfr.dev.data.model.request.LoginRequest
import trackerr.rezyfr.dev.data.model.request.RegisterRequest
import trackerr.rezyfr.dev.data.model.response.ErrorResponse
import trackerr.rezyfr.dev.service.UserService
import trackerr.rezyfr.dev.util.PasswordManager

interface UserController {
    suspend fun register(call: ApplicationCall)
    suspend fun login(call: ApplicationCall)
    suspend fun findUserByEmail(email: String): User?
}

class UserControllerImpl(
    private val userService: UserService,
    private val passwordManager: PasswordManager
) : UserController {
    override suspend fun register(call: ApplicationCall) {
        try {
            call.receive<RegisterRequest>().apply {
                userService.createUser(User(email = email, hashPassword = passwordManager.hash(password), name = name)).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong",false))
        }
    }

    override suspend fun login(call: ApplicationCall) {
        try {
            call.receive<LoginRequest>().apply {
                userService.authenticate(email = email, password = password).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }

    override suspend fun findUserByEmail(email: String): User? {
        return userService.findUserByEmail(email)
    }
}