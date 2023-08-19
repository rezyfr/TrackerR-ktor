package trackerr.rezyfr.dev.service

import trackerr.rezyfr.dev.authentication.JwtService
import trackerr.rezyfr.dev.data.model.User
import trackerr.rezyfr.dev.data.model.response.BaseResponse
import trackerr.rezyfr.dev.repository.CategoryRepository
import trackerr.rezyfr.dev.repository.UserRepository
import trackerr.rezyfr.dev.util.PasswordManager

interface UserService {
    suspend fun createUser(user: User): BaseResponse<String>
    suspend fun authenticate(email: String, password: String): BaseResponse<String>
    suspend fun findUserByEmail(email: String): User?
}

class UserServiceImpl (
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val passwordManager: PasswordManager,
    private val jwtService: JwtService
) : UserService{
    override suspend fun createUser(user: User): BaseResponse<String> {
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
        return BaseResponse(true, "Successfully registered", jwtService.generateToken(user))
    }

    override suspend fun authenticate(email: String, password: String): BaseResponse<String> {
        userRepository.findUserByEmail(email)?.let {
          if (it.hashPassword == passwordManager.hash(password)) {
              return BaseResponse(true, "Successfully logged in", jwtService.generateToken(it))
          } else {
              throw IllegalArgumentException("Password is incorrect")
          }
        }
        throw IllegalArgumentException("User with email $email not found")
    }

    override suspend fun findUserByEmail(email: String): User? {
        return userRepository.findUserByEmail(email)
    }
}