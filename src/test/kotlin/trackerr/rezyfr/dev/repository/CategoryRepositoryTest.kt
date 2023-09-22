package trackerr.rezyfr.dev.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.db.table.UserTable
import trackerr.rezyfr.dev.mapper.CategoryMapper
import trackerr.rezyfr.dev.mapper.IconMapper
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryRepositoryTest : BaseRepositoryTest() {

    lateinit var categoryRepository: CategoryRepository
    lateinit var userRepository: UserRepository
    lateinit var iconRepository: IconRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        categoryRepository = CategoryRepositoryImpl(CategoryMapper(), IconMapper())
        userRepository = UserRepositoryImpl()
        iconRepository = IconRepositoryImpl(IconMapper())
    }

    @Test
    fun `test add category`() {
        withTables(CategoryTable, UserTable) {
            userRepository.addUser(user)
            iconRepository.addIcon(icon)
            val category = Category("test", user.email, CategoryType.EXPENSE, 1, 0xffffffff)

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
            iconRepository.addIcon(icon)
            val emptyResult = categoryRepository.getCategories(user.email, CategoryType.EXPENSE)
            assert(emptyResult.isEmpty())

            val category = Category("test", user.email, CategoryType.EXPENSE, 1,0xffffffff)

            categoryRepository.addCategory(category).let {
                val result = categoryRepository.getCategories(user.email, category.type)
                assert(result.isNotEmpty())
                assert(result[0].name == category.name)
                assert(result[0].type == category.type)
            }
        }
    }

//    @Test
//    fun `test populate starter categories`() {
//        withTables(CategoryTable, UserTable) {
//            userRepository.addUser(user)
//            iconRepository.addIcon(icon)
//            categoryRepository.populateStarterCategories(user.email)
//            val result = categoryRepository.getCategories(user.email, CategoryType.EXPENSE)
//            val expected = Category.getInitialCategories(user.email).filter { it.type == CategoryType.EXPENSE }
//            result.forEachIndexed { index, categoryResponse ->
//                assert(categoryResponse.name == expected[index].name)
//                assert(categoryResponse.type == expected[index].type)
//            }
//        }
//    }
}