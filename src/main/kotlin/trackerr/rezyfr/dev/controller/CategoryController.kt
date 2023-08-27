package trackerr.rezyfr.dev.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import trackerr.rezyfr.dev.model.Category
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.User
import trackerr.rezyfr.dev.model.request.CreateCategoryRequest
import trackerr.rezyfr.dev.model.response.ErrorResponse
import trackerr.rezyfr.dev.service.CategoryService

interface CategoryController {
     suspend fun addCategory(call: ApplicationCall)
     suspend fun getCategories(call: ApplicationCall)
}

class CategoryControllerImpl(
    private val categoryService: CategoryService
) : CategoryController {
    override suspend fun addCategory(call: ApplicationCall) {
        try {
            call.receive<CreateCategoryRequest>().apply {
                call.principal<User>()!!.let {
                    categoryService.addCategory(
                        Category(
                            name = name,
                            userEmail = it.email,
                            type = type
                        )
                    ).let {
                        call.respond(HttpStatusCode.OK, it)
                    }
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }

    override suspend fun getCategories(call: ApplicationCall) {
        try {
            call.principal<User>()!!.let {
                call.parameters["type"]?.let { type ->
                    categoryService.getCategories(it.email, CategoryType.valueOf(type)).let {
                        call.respond(HttpStatusCode.OK, it)
                    }
                } ?: throw IllegalArgumentException("Type is required")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }
}