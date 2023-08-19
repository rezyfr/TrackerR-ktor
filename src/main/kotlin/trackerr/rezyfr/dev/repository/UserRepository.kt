package trackerr.rezyfr.dev.repository

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import trackerr.rezyfr.dev.data.model.User
import trackerr.rezyfr.dev.data.table.UserTable
import trackerr.rezyfr.dev.db.DatabaseFactory.dbQuery

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun findUserByEmail(email: String): User?
}

class UserRepositoryImpl : UserRepository {
    override suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert {
                it[email] = user.email
                it[hashPassword] = user.hashPassword
                it[name] = user.name
            }
        }
    }

    override suspend fun findUserByEmail(email: String): User? {
        return dbQuery {
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