package trackerr.rezyfr.dev.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import trackerr.rezyfr.dev.db.table.UserTable
import trackerr.rezyfr.dev.model.User

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest : BaseRepositoryTest() {

    lateinit var userRepository: UserRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        userRepository = UserRepositoryImpl()
    }

    @Test
    fun `test add user`()  {
        withTables(UserTable) {
            val user = User("test@gmail.com", "test", "test")
            userRepository.addUser(user)

            userRepository.findUserByEmail(user.email)?.let {
                assert(it.email == user.email)
                assert(it.hashPassword == user.hashPassword)
                assert(it.name == user.name)
            } ?: assert(false)
        }
    }

    @Test
    fun `test find user by email`() {
        withTables(UserTable) {
            userRepository.findUserByEmail("notfound@gmail.com").let {
                assert(it == null)
            }
        }
    }
}