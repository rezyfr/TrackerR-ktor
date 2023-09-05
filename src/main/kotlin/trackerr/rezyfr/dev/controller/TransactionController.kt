package trackerr.rezyfr.dev.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import trackerr.rezyfr.dev.model.Transaction
import trackerr.rezyfr.dev.model.User
import trackerr.rezyfr.dev.model.request.CreateTransactionRequest
import trackerr.rezyfr.dev.model.response.ErrorResponse
import trackerr.rezyfr.dev.service.TransactionService

interface TransactionController {
     suspend fun addTransaction(call: ApplicationCall)
     suspend fun getRecentTransactions(call: ApplicationCall)
     suspend fun getMonthlySummary(call: ApplicationCall)
}

class TransactionControllerImpl(
    private val transactionService: TransactionService
) : TransactionController {
    override suspend fun addTransaction(call: ApplicationCall) {
        try {
            call.receive<CreateTransactionRequest>().apply {
                call.principal<User>()!!.let {
                    transactionService.addTransaction(
                        Transaction(
                            amount = amount.toBigDecimal(),
                            description = description,
                            categoryId = categoryId,
                            walletId = walletId,
                            createdDate = createdDate
                        ),
                        it.email
                    ).let {
                        call.respond(HttpStatusCode.OK, it)
                    }
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }

    override suspend fun getRecentTransactions(call: ApplicationCall) {
        try {
            call.principal<User>()!!.let {
                transactionService.getRecentTransactions(it.email).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }

    override suspend fun getMonthlySummary(call: ApplicationCall) {
        try {
            call.principal<User>()!!.let { user ->
                call.parameters["month"]?.let {
                    transactionService.getMonthlySummary(it.toInt(), user.email).let {
                        call.respond(HttpStatusCode.OK, it)
                    }
                } ?: run {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Month is required", false))
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }
}