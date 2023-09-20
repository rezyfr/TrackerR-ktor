package trackerr.rezyfr.dev.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.SortOrder
import trackerr.rezyfr.dev.model.CategoryType
import trackerr.rezyfr.dev.model.Granularity
import trackerr.rezyfr.dev.model.Transaction
import trackerr.rezyfr.dev.model.User
import trackerr.rezyfr.dev.model.request.CreateTransactionRequest
import trackerr.rezyfr.dev.model.response.ErrorResponse
import trackerr.rezyfr.dev.model.response.transaction.TransactionWithDateResponse
import trackerr.rezyfr.dev.service.TransactionService
import java.math.BigDecimal

interface TransactionController {
    suspend fun addTransaction(call: ApplicationCall)
    suspend fun getRecentTransactions(call: ApplicationCall)
    suspend fun getMonthlySummary(call: ApplicationCall)
    suspend fun getTransactionFrequency(call: ApplicationCall)
    suspend fun getTransactionWithDate(call: ApplicationCall)
    suspend fun getTransactionReport(call: ApplicationCall)
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
                            amount = amount,
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

    override suspend fun getTransactionFrequency(call: ApplicationCall) {
        try {
            call.principal<User>()!!.let { user ->
                call.parameters["granularity"]?.let {
                    transactionService.getTransactionFrequency(Granularity.valueOf(it)).let {
                        call.respond(HttpStatusCode.OK, it)
                    }
                } ?: run {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Granularity is required", false))
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }

    override suspend fun getTransactionWithDate(call: ApplicationCall) {
        try {
            call.principal<User>()!!.let { user ->
                val type =
                    if (call.parameters["type"] != null) CategoryType.valueOf(call.parameters["type"]!!) else null
                val order =
                    if (call.parameters["sortOrder"] != null) SortOrder.valueOf(call.parameters["sortOrder"]!!) else SortOrder.ASC
                val categoryId =
                    if (call.parameters["categoryId"] != null) call.parameters["categoryId"]!!.split(",").map { it.toInt() } else null
                val month =
                    if (call.parameters["month"] != null) call.parameters["month"]!!.toInt() else null
                transactionService.getTransactionWithDate(
                    type,
                    order,
                    categoryId,
                    month
                ).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }

    override suspend fun getTransactionReport(call: ApplicationCall) {
        try {
            call.principal<User>()!!.let { user ->
                transactionService.getTransactionReport(user.email).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }
}