package trackerr.rezyfr.dev.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun hikari(application: Application): HikariDataSource {
        val dbConfig = application.environment.config.config("database")
        val dbUser = dbConfig.property("user").getString()
        val dbPassword = dbConfig.property("password").getString()
        val poolSize = dbConfig.property("pool_size").getString().toInt()
        val driver = dbConfig.property("driver").getString()
        val dbName = dbConfig.property("db_name").getString()
        val dbUrl = dbConfig.property("db_url").getString()
        val url = "jdbc:postgresql://$dbUrl/$dbName"

        val config = HikariConfig()
        config.jdbcUrl = url
        config.maximumPoolSize = poolSize
        config.driverClassName = driver
        config.username = dbUser
        config.password = dbPassword
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)
    }
}


fun Application.configureDatabase() {
    val pool = DatabaseFactory.hikari(this)
    Database.connect(pool)

    val flyway = Flyway.configure()
        .dataSource(pool)
        .baselineOnMigrate(true)
        .load()

    try {
        flyway.info()
        flyway.migrate()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
