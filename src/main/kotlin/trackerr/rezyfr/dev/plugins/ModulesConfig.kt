package trackerr.rezyfr.dev.plugins

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import trackerr.rezyfr.dev.authentication.JwtService
import trackerr.rezyfr.dev.controller.*
import trackerr.rezyfr.dev.mapper.CategoryMapper
import trackerr.rezyfr.dev.repository.*
import trackerr.rezyfr.dev.service.*
import trackerr.rezyfr.dev.util.PasswordManager


val authModule = DI.Module("AUTH") {
    bindSingleton { PasswordManager(instance()) }
    bindSingleton { JwtService(instance()) }
}

val mapperModule = DI.Module("MAPPER") {
    bindSingleton { CategoryMapper() }
}

val userModule = DI.Module("USER") {
    bindSingleton<UserService> { UserServiceImpl(instance(), instance(), instance(), instance()) }
    bindSingleton<UserRepository> { UserRepositoryImpl() }
    bindSingleton<UserController> { UserControllerImpl(instance(), instance()) }
}

val walletModule = DI.Module("WALLET") {
    bindSingleton<WalletService> { WalletServiceImpl(instance()) }
    bindSingleton<WalletRepository> { WalletRepositoryImpl() }
    bindSingleton<WalletController> { WalletControllerImpl(instance()) }
}

val categoryModule = DI.Module("CATEGORY") {
    bindSingleton<CategoryService> { CategoryServiceImpl(instance()) }
    bindSingleton<CategoryRepository> { CategoryRepositoryImpl(instance()) }
    bindSingleton<CategoryController> { CategoryControllerImpl(instance()) }
}

val transactionModule = DI.Module("TRANSACTION") {
    bindSingleton<TransactionService> { TransactionServiceImpl(instance(), instance(), instance()) }
    bindSingleton<TransactionRepository> { TransactionRepositoryImpl() }
    bindSingleton<TransactionController> { TransactionControllerImpl(instance()) }
}

internal val kodein = DI {

}