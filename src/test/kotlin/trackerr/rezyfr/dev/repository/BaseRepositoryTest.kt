package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import trackerr.rezyfr.dev.db.table.CategoryTable
import trackerr.rezyfr.dev.db.table.TransactionTable
import trackerr.rezyfr.dev.db.table.UserTable
import trackerr.rezyfr.dev.db.table.WalletTable
import trackerr.rezyfr.dev.model.User

abstract class BaseRepositoryTest {
    @BeforeEach
    open fun setUp() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver", user = "root", password = "")
    }

    open val user = User("test@gmail.com", "test", "test")

    fun withTables(vararg tables: Table, statement: Transaction.() -> Unit) {
        transaction {
            SchemaUtils.create(*tables)

            statement()
            commit()
        }
    }

    @AfterAll
    open fun tearDown() {
        transaction {
            SchemaUtils.drop(*arrayOf(UserTable, WalletTable, CategoryTable, TransactionTable))
        }
    }
}