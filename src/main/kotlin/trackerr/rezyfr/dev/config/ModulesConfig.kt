package trackerr.rezyfr.dev.config

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import trackerr.rezyfr.dev.util.JwtService
import trackerr.rezyfr.dev.controller.*
import trackerr.rezyfr.dev.mapper.CategoryMapper
import trackerr.rezyfr.dev.mapper.IconMapper
import trackerr.rezyfr.dev.mapper.TransactionMapper
import trackerr.rezyfr.dev.mapper.WalletMapper
import trackerr.rezyfr.dev.repository.*
import trackerr.rezyfr.dev.service.*
import trackerr.rezyfr.dev.util.PasswordManager


val authModule = DI.Module("AUTH") {
    bindSingleton { PasswordManager(instance()) }
    bindSingleton { JwtService(instance()) }
}

val mapperModule = DI.Module("MAPPER") {
    bindSingleton { CategoryMapper() }
    bindSingleton { WalletMapper() }
    bindSingleton { TransactionMapper() }
    bindSingleton { IconMapper() }
}

val userModule = DI.Module("USER") {
    bindSingleton<UserService> { UserServiceImpl(instance(), instance(), instance(), instance()) }
    bindSingleton<UserRepository> { UserRepositoryImpl() }
    bindSingleton<UserController> { UserControllerImpl(instance(), instance()) }
}

val walletModule = DI.Module("WALLET") {
    bindSingleton<WalletService> { WalletServiceImpl(instance()) }
    bindSingleton<WalletRepository> { WalletRepositoryImpl(instance()) }
    bindSingleton<WalletController> { WalletControllerImpl(instance()) }
}

val categoryModule = DI.Module("CATEGORY") {
    bindSingleton<CategoryService> { CategoryServiceImpl(instance()) }
    bindSingleton<CategoryRepository> { CategoryRepositoryImpl(instance()) }
    bindSingleton<CategoryController> { CategoryControllerImpl(instance()) }
}

val transactionModule = DI.Module("TRANSACTION") {
    bindSingleton<TransactionService> { TransactionServiceImpl(instance(), instance(), instance()) }
    bindSingleton<TransactionRepository> { TransactionRepositoryImpl(instance()) }
    bindSingleton<TransactionController> { TransactionControllerImpl(instance()) }
}

val iconModule = DI.Module("ICON") {
    bindSingleton<IconService> { IconServiceImpl(instance()) }
    bindSingleton<IconRepository> { IconRepositoryImpl(instance()) }
    bindSingleton<IconController> { IconControllerImpl(instance()) }
}