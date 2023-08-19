package trackerr.rezyfr.dev.plugins

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import trackerr.rezyfr.dev.authentication.JwtService
import trackerr.rezyfr.dev.controller.*
import trackerr.rezyfr.dev.repository.*
import trackerr.rezyfr.dev.service.*
import trackerr.rezyfr.dev.util.PasswordManager

object ModulesConfig {
    private val authModule = DI.Module("AUTH") {
        bindSingleton { PasswordManager }
        bindSingleton { JwtService }
    }

    private val userModule = DI.Module("USER") {
        bindSingleton<UserService> { UserServiceImpl(instance(), instance(), instance(), instance()) }
        bindSingleton<UserRepository> { UserRepositoryImpl() }
        bindSingleton<UserController> { UserControllerImpl(instance(), instance()) }
    }

    private val walletModule = DI.Module("WALLET") {
        bindSingleton<WalletService> { WalletServiceImpl(instance()) }
        bindSingleton<WalletRepository> { WalletRepositoryImpl() }
        bindSingleton<WalletController> { WalletControllerImpl(instance()) }
    }

    private val categoryModule = DI.Module("CATEGORY") {
        bindSingleton<CategoryService> { CategoryServiceImpl(instance()) }
        bindSingleton<CategoryRepository> { CategoryRepositoryImpl() }
        bindSingleton<CategoryController> { CategoryControllerImpl(instance()) }
    }

    internal val kodein = DI {
        import(authModule)
        import(userModule)
        import(walletModule)
        import(categoryModule)
    }
}