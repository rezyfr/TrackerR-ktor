package trackerr.rezyfr.dev.route

import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.routing.*
import trackerr.rezyfr.dev.controller.WalletController

const val WALLET = "$API_VERSION/wallet"
const val CREATE_WALLET_REQUEST = "$WALLET/create"
const val GET_WALLET_REQUEST = WALLET
const val UPDATE_WALLET_BALANCE_REQUEST = "$WALLET/update/balance"

@Location(CREATE_WALLET_REQUEST)
class CreateWalletRoute

@Location(GET_WALLET_REQUEST)
class GetWalletRoute

@Location(UPDATE_WALLET_BALANCE_REQUEST)
class UpdateWalletBalanceRoute

fun Route.walletRoutes(
    walletController: WalletController,
) {
    authenticate {
        post<CreateWalletRoute> { walletController.addWallet(context) }
        get<GetWalletRoute> { walletController.getWallets(context) }
        post<UpdateWalletBalanceRoute> { walletController.updateWalletBalance(context) }
    }
}