package trackerr.rezyfr.dev.service

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import trackerr.rezyfr.dev.model.User
import trackerr.rezyfr.dev.repository.CategoryRepository
import trackerr.rezyfr.dev.repository.UserRepository
import trackerr.rezyfr.dev.util.JwtService
import trackerr.rezyfr.dev.util.PasswordManager


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserServiceTest {
    private lateinit var userService: UserService

    private val userRepository: UserRepository = mockk()
    private val categoryRepository: CategoryRepository = mockk()
    private val passwordManager: PasswordManager = mockk()
    private val jwtService: JwtService = mockk()

    private val user = User("test@mail.com", "test", "test")

    @BeforeEach
    fun setUp() {
        userService = UserServiceImpl(userRepository, categoryRepository, passwordManager, jwtService)
    }

    @Test
    fun `test success create user`() {
        coEvery { userRepository.findUserByEmail(user.email) } returns null
        coEvery { userRepository.addUser(user) } returns Unit
        coEvery { categoryRepository.populateStarterCategories(user.email) } returns Unit
        coEvery { jwtService.generateToken(user) } returns "token"

        userService.createUser(user)

        coVerify(atLeast = 1) {
            userRepository.addUser(user)
            categoryRepository.populateStarterCategories(user.email)
            jwtService.generateToken(user)
        }
    }

    @Test
    fun `test failed create user with existed email`() {
        coEvery { userRepository.findUserByEmail(user.email) } returns user

        assertThrows<IllegalArgumentException> {
            userService.createUser(user)
        }
    }

    @Test
    fun `test failed create user with empty email`() {
        coEvery { userRepository.findUserByEmail("") } returns null

        assertThrows<IllegalArgumentException> {
            userService.createUser(user.copy(email = ""))
        }

        coVerify(exactly = 1) { userRepository.findUserByEmail("") }
    }

    @Test
    fun `test failed create user with empty name`() {
        coEvery { userRepository.findUserByEmail(user.email) } returns null

        assertThrows<IllegalArgumentException> {
            userService.createUser(user.copy(name = ""))
        }

        coVerify(exactly = 1) { userRepository.findUserByEmail(user.email) }
    }

    @Test
    fun `test failed create user with empty password`() {
        coEvery { userRepository.findUserByEmail(user.email) } returns null

        assertThrows<IllegalArgumentException> {
            userService.createUser(user.copy(hashPassword = ""))
        }

        coVerify(exactly = 1) { userRepository.findUserByEmail(user.email) }
    }

    @Test
    fun `test success authenticate`() {
        coEvery { userRepository.findUserByEmail(user.email) } returns user
        coEvery { passwordManager.hash(user.hashPassword) } returns user.hashPassword
        coEvery { jwtService.generateToken(user) } returns "token"

        userService.authenticate(user.email, user.hashPassword)

        coVerify(exactly = 1) {
            userRepository.findUserByEmail(user.email)
            passwordManager.hash(user.hashPassword)
            jwtService.generateToken(user)
        }
    }

    @Test
    fun `test incorrect password when authenticate`() {
        coEvery { userRepository.findUserByEmail(user.email) } returns user
        coEvery { passwordManager.hash(user.hashPassword) } returns "incorrectpassdummy"

        assertThrows<IllegalArgumentException> {
            userService.authenticate(user.email, user.hashPassword)
        }

        coVerify(exactly = 1) {
            userRepository.findUserByEmail(user.email)
            passwordManager.hash(user.hashPassword)
        }
    }

    @Test
    fun `test no user found when authenticate`() {
        coEvery { userRepository.findUserByEmail(user.email) } returns null

        assertThrows<IllegalArgumentException> {
            userService.authenticate(user.email, user.hashPassword)
        }

        coVerify(exactly = 1) { userRepository.findUserByEmail(user.email) }
        coVerify(exactly = 0) {
            passwordManager.hash(user.hashPassword)
            jwtService.generateToken(user)
        }
    }

    @Test
    fun `test find user by email`() {
        coEvery { userRepository.findUserByEmail(user.email) } returns user

        userService.findUserByEmail(user.email)

        coVerify(exactly = 1) { userRepository.findUserByEmail(user.email) }
    }

    @Test
    fun `test find user by email with no user found`() {
        coEvery { userRepository.findUserByEmail(user.email) } returns null

        userService.findUserByEmail(user.email)

        coVerify(exactly = 1) { userRepository.findUserByEmail(user.email) }
    }
}