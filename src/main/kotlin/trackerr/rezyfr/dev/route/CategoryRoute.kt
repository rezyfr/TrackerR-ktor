package trackerr.rezyfr.dev.route

import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.routing.*
import trackerr.rezyfr.dev.controller.CategoryController

const val CATEGORY = "$API_VERSION/category"
const val ADD_CATEGORY_REQUEST = CATEGORY
const val GET_CATEGORY_REQUEST = CATEGORY

@Location(ADD_CATEGORY_REQUEST)
class AddCategoryRoute

@Location(GET_CATEGORY_REQUEST)
class GetCategoryRoute

fun Route.categoryRoutes(
    categoryController: CategoryController,
) {
    authenticate {
        post<AddCategoryRoute> { categoryController.addCategory(context) }
        get<GetCategoryRoute> { categoryController.getCategories(context) }
    }
}
