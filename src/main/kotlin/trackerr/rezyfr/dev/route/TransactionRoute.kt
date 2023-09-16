package trackerr.rezyfr.dev.route

import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.routing.*
import trackerr.rezyfr.dev.controller.TransactionController

const val TRANSACTION = "$API_VERSION/transaction"
const val CREATE_TRANSACTION_REQUEST = TRANSACTION
const val GET_RECENT_TRANSACTION_REQUEST = "$TRANSACTION/recent"
const val GET_MONTHLY_SUMMARY_REQUEST = "$TRANSACTION/summary"
const val GET_TRANSACTION_FREQUENCY_REQUEST = "$TRANSACTION/frequency"
const val GET_TRANSACTION_WITH_DATE_REQUEST = "$TRANSACTION/with-date"

@Location(CREATE_TRANSACTION_REQUEST)
class CreateTransactionRoute

@Location(GET_RECENT_TRANSACTION_REQUEST)
class GetRecentTransactionRoute

@Location(GET_MONTHLY_SUMMARY_REQUEST)
class GetMonthlySummaryRoute

@Location(GET_TRANSACTION_FREQUENCY_REQUEST)
class GetTransactionFrequencyRoute

@Location(GET_TRANSACTION_WITH_DATE_REQUEST)
class GetTransactionWithDateRoute

fun Route.transactionRoutes(
    transactionController: TransactionController,
) {
    authenticate {
        post<CreateTransactionRoute> { transactionController.addTransaction(context) }
        get<GetRecentTransactionRoute> { transactionController.getRecentTransactions(context) }
        post<GetMonthlySummaryRoute> { transactionController.getMonthlySummary(context) }
        get<GetTransactionFrequencyRoute> { transactionController.getTransactionFrequency(context) }
        get<GetTransactionWithDateRoute> { transactionController.getTransactionWithDate(context) }
    }
}