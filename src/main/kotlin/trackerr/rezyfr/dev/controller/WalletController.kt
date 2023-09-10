package trackerr.rezyfr.dev.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import trackerr.rezyfr.dev.model.User
import trackerr.rezyfr.dev.model.Wallet
import trackerr.rezyfr.dev.model.request.CreateWalletRequest
import trackerr.rezyfr.dev.model.request.UpdateWalletBalanceRequest
import trackerr.rezyfr.dev.model.response.ErrorResponse
import trackerr.rezyfr.dev.service.WalletService

interface WalletController {
     suspend fun addWallet(call: ApplicationCall)
     suspend fun getWallets(call: ApplicationCall)
     suspend fun updateWalletBalance(call: ApplicationCall)
     suspend fun getWalletBalance(call: ApplicationCall)
}

class WalletControllerImpl(
    private val walletService: WalletService
) : WalletController {
    override suspend fun addWallet(call: ApplicationCall) {
        try {
            call.receive<CreateWalletRequest>().apply {
                call.principal<User>()!!.let {
                    walletService.addWallet(
                        Wallet(
                            name = name,
                            balance = balance,
                            userEmail = it.email,
                            iconId = iconId
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

    override suspend fun getWallets(call: ApplicationCall) {
        try {
            call.principal<User>()!!.let {
                walletService.getWalletByUserEmail(it.email).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }

    override suspend fun updateWalletBalance(call: ApplicationCall) {
        try {
            call.principal<User>()!!.let {
                call.receive<UpdateWalletBalanceRequest>().apply {
                    walletService.updateWalletBalance(
                        id, balance, it.email
                    ).let {
                        call.respond(HttpStatusCode.OK, it)
                    }
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }

    override suspend fun getWalletBalance(call: ApplicationCall) {
        try {
            call.principal<User>()!!.let {
                walletService.getWalletBalance(it.email).let { balance ->
                    call.respond(HttpStatusCode.OK, balance)
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Something went wrong", false))
        }
    }
}