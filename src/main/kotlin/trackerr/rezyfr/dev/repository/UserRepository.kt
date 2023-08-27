package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import trackerr.rezyfr.dev.db.table.UserTable
import trackerr.rezyfr.dev.model.User

interface UserRepository {
     fun addUser(user: User)
     fun findUserByEmail(email: String): User?
}

class UserRepositoryImpl : UserRepository {
    override fun addUser(user: User) {
        transaction {
            UserTable.insert {
                it[email] = user.email
                it[hashPassword] = user.hashPassword
                it[name] = user.name
            }
        }
    }

    override fun findUserByEmail(email: String): User? {
        return transaction {
            UserTable.select {
                UserTable.email.eq(email)
            }.map(::rowToUser).singleOrNull()
        }
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) return null

        return User(
            email = row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            name = row[UserTable.name]
        )
    }
}