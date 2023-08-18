package trackerr.rezyfr.dev.plugins

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import trackerr.rezyfr.dev.authentication.JwtService
import trackerr.rezyfr.dev.controller.UserController
import trackerr.rezyfr.dev.controller.UserControllerImpl
import trackerr.rezyfr.dev.controller.WalletController
import trackerr.rezyfr.dev.controller.WalletControllerImpl
import trackerr.rezyfr.dev.repository.UserRepository
import trackerr.rezyfr.dev.repository.UserRepositoryImpl
import trackerr.rezyfr.dev.repository.WalletRepository
import trackerr.rezyfr.dev.repository.WalletRepositoryImpl
import trackerr.rezyfr.dev.service.UserService
import trackerr.rezyfr.dev.service.UserServiceImpl
import trackerr.rezyfr.dev.service.WalletService
import trackerr.rezyfr.dev.service.WalletServiceImpl
import trackerr.rezyfr.dev.util.PasswordManager

object ModulesConfig {
    private val authModule = DI.Module("AUTH") {
        bindSingleton { PasswordManager }
        bindSingleton { JwtService }
    }

    private val userModule = DI.Module("USER") {
        bindSingleton<UserService> { UserServiceImpl(instance(), instance(), instance()) }
        bindSingleton<UserRepository> { UserRepositoryImpl() }
        bindSingleton<UserController> { UserControllerImpl(instance()) }
    }

    private val walletModule = DI.Module("WALLET") {
        bindSingleton<WalletService> { WalletServiceImpl(instance()) }
        bindSingleton<WalletRepository> { WalletRepositoryImpl() }
        bindSingleton<WalletController> { WalletControllerImpl(instance()) }
    }

    internal val kodein = DI {
        import(authModule)
        import(userModule)
        import(walletModule)
    }
}