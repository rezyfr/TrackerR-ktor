package trackerr.rezyfr.dev.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.response.CategoryResponse
import trackerr.rezyfr.dev.repository.CategoryRepository
import java.sql.SQLException

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CategoryServiceTest {

    private lateinit var categoryService: CategoryService

    private val categoryRepository: CategoryRepository = mockk()

    @BeforeEach
    fun setUp() {
        categoryService = CategoryServiceImpl(categoryRepository)
    }

    @Test
    fun `test add category`() {
        val catResponse = CategoryResponse(1, "test", CategoryType.EXPENSE)
        coEvery { categoryRepository.addCategory(any()) } returns catResponse

        val result = categoryService.addCategory(Category("test", "email@mail.com", CategoryType.EXPENSE))
        coVerify(exactly = 1) { categoryRepository.addCategory(any()) }

        assert(result.status == true)
    }

    @Test
    fun `test failed add category`() {
        coEvery { categoryRepository.addCategory(any()) } throws SQLException()

        assertThrows<Exception> {
            categoryService.addCategory(Category("test", "email@email.com", CategoryType.EXPENSE))
        }
    }

    @Test
    fun `test get categories`() {
        val catResponse = CategoryResponse(1, "test", CategoryType.EXPENSE)
        coEvery { categoryRepository.getCategories(any(), any()) } returns listOf(catResponse)

        val result = categoryService.getCategories("mail@mail.com", CategoryType.EXPENSE)
        coVerify(exactly = 1) { categoryRepository.getCategories(any(), any()) }

        assert(result.status == true)
        result.data?.let {
            assert(it.isNotEmpty())
            assert(it[0].name == catResponse.name)
            assert(it[0].id == catResponse.id)
            assert(it[0].type == catResponse.type)
        }
    }
}