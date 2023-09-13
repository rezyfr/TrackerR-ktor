package trackerr.rezyfr.dev.service

import trackerr.rezyfr.dev.model.User
import trackerr.rezyfr.dev.model.response.BaseResponse
import trackerr.rezyfr.dev.model.response.TokenResponse
import trackerr.rezyfr.dev.repository.CategoryRepository
import trackerr.rezyfr.dev.repository.UserRepository
import trackerr.rezyfr.dev.util.JwtService
import trackerr.rezyfr.dev.util.PasswordManager

interface UserService {
     fun createUser(user: User): BaseResponse<TokenResponse>
     fun authenticate(email: String, password: String): BaseResponse<TokenResponse>
     fun findUserByEmail(email: String): User?
     fun refreshToken(email: String, refreshToken: String): BaseResponse<TokenResponse>
}

class UserServiceImpl (
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val passwordManager: PasswordManager,
    private val jwtService: JwtService,
) : UserService{
    override fun createUser(user: User): BaseResponse<TokenResponse> {
        userRepository.findUserByEmail(user.email)?.let {
            throw IllegalArgumentException("User with email ${user.email} already exists")
        }
        if (user.email.isEmpty()) {
            throw IllegalArgumentException("Email cannot be empty")
        }
        if (user.name.isEmpty()) {
            throw IllegalArgumentException("Name cannot be empty")
        }
        if (user.hashPassword.isEmpty()) {
            throw IllegalArgumentException("Password cannot be empty")
        }
        userRepository.addUser(user)
        categoryRepository.populateStarterCategories(user.email)
        return BaseResponse(true, "Successfully registered", jwtService.createToken(user))
    }

    override fun authenticate(email: String, password: String): BaseResponse<TokenResponse> {
        userRepository.findUserByEmail(email)?.let {
          if (it.hashPassword == passwordManager.hash(password)) {
              return BaseResponse(true, "Successfully logged in", jwtService.createToken(it))
          } else {
              throw IllegalArgumentException("Password is incorrect")
          }
        }
        throw IllegalArgumentException("User with email $email not found")
    }

    override fun findUserByEmail(email: String): User? {
        return userRepository.findUserByEmail(email)
    }

    override fun refreshToken(email: String, refreshToken: String): BaseResponse<TokenResponse> {
        if (jwtService.isTokenExpired(refreshToken)) throw IllegalArgumentException("Token is expired")
        if (jwtService.getTokenType(refreshToken) != "refresh_token") throw IllegalArgumentException("Wrong token type")
        jwtService.verifier.verify(refreshToken).claims["email"]?.asString()?.let {
            if (it == email) {
                userRepository.findUserByEmail(email)?.let { user ->
                    return BaseResponse(true, "Successfully refreshed token", jwtService.createToken(user))
                }
            }
        }
        throw IllegalArgumentException("Token is invalid")
    }
}