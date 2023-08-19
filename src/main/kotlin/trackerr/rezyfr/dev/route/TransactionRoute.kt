package trackerr.rezyfr.dev.route

import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.routing.*
import trackerr.rezyfr.dev.controller.TransactionController

const val TRANSACTION = "$API_VERSION/transaction"
const val CREATE_TRANSACTION_REQUEST = TRANSACTION
const val GET_RECENT_TRANSACTION_REQUEST = "$TRANSACTION/recent"

@Location(CREATE_TRANSACTION_REQUEST)
class CreateTransactionRoute

@Location(GET_RECENT_TRANSACTION_REQUEST)
class GetRecentTransactionRoute

fun Route.transactionRoutes(
    transactionController: TransactionController,
) {
    authenticate {
        post<CreateTransactionRoute> { transactionController.addTransaction(context) }
        get<GetRecentTransactionRoute> { transactionController.getRecentTransactions(context) }
    }
}