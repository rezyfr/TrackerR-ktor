package trackerr.rezyfr.dev.route

import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.routing.*
import trackerr.rezyfr.dev.controller.UserController

const val API_VERSION = "/v1"
const val USER = "$API_VERSION/user"
const val REGISTER_REQUEST = "$USER/register"
const val LOGIN_REQUEST = "$USER/login"
const val CHECK_TOKEN = "$USER/check-token"

@Location(REGISTER_REQUEST)
class UserRegisterRoute

@Location(LOGIN_REQUEST)
class UserLoginRoute

@Location(CHECK_TOKEN)
class CheckTokenRoute

fun Route.userRoutes(
    userController: UserController,
) {
    post<UserRegisterRoute> { userController.register(context) }
    post<UserLoginRoute> { userController.login(context) }
    authenticate {
        get<CheckTokenRoute> { userController.checkToken(context) }
    }
}