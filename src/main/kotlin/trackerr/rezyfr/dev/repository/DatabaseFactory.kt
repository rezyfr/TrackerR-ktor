package trackerr.rezyfr.dev.repository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import trackerr.rezyfr.dev.data.table.UserTable

object DatabaseFactory {
    fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER")
        config.jdbcUrl =
            "${System.getenv("DB_URL")}?user=${System.getenv("DB_USER")}&password=${System.getenv("DB_PASSWORD")}"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}


fun Application.configureDatabase() {
    Database.connect(DatabaseFactory.hikari())

    transaction {
        SchemaUtils.create(UserTable)
    }
}
