package trackerr.rezyfr.dev.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.db.table.UserTable
import trackerr.rezyfr.dev.mapper.CategoryMapper
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryRepositoryTest : BaseRepositoryTest() {

    lateinit var categoryRepository: CategoryRepository
    lateinit var userRepository: UserRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        categoryRepository = CategoryRepositoryImpl(CategoryMapper())
        userRepository = UserRepositoryImpl()
    }

    @Test
    fun `test add category`() {
        withTables(CategoryTable, UserTable) {
            userRepository.addUser(user)
            val category = Category("test", user.email, CategoryType.EXPENSE)

            val nullResult = categoryRepository.getCategoryById(0, user.email)
            assert(nullResult == null)

            categoryRepository.addCategory(category).let {
                val result = categoryRepository.getCategoryById(it.id, user.email)
                assert(result != null)
                assert(result!!.name == category.name)
                assert(result.type == category.type)
            }
        }
    }

    @Test
    fun `test find category by type`() {
        withTables(CategoryTable, UserTable) {
            userRepository.addUser(user)
            val emptyResult = categoryRepository.getCategories(user.email, CategoryType.EXPENSE)
            assert(emptyResult.isEmpty())

            val category = Category("test", user.email, CategoryType.EXPENSE)

            categoryRepository.addCategory(category).let {
                val result = categoryRepository.getCategories(user.email, category.type)
                assert(result.isNotEmpty())
                assert(result[0].name == category.name)
                assert(result[0].type == category.type)
            }
        }
    }

    @Test
    fun `test populate starter categories`() {
        withTables(CategoryTable, UserTable) {
            userRepository.addUser(user)
            categoryRepository.populateStarterCategories(user.email)
            val result = categoryRepository.getCategories(user.email, CategoryType.EXPENSE)
            val expected = Category.getInitialCategories(user.email).filter { it.type == CategoryType.EXPENSE }
            result.forEachIndexed { index, categoryResponse ->
                assert(categoryResponse.name == expected[index].name)
                assert(categoryResponse.type == expected[index].type)
            }
        }
    }
}