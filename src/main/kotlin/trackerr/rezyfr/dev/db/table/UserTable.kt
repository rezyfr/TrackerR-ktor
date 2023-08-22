package trackerr.rezyfr.dev.db.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {

    val email = varchar("email", 128)
    val hashPassword = varchar("hash_password", 512)
    val name = varchar("name", 128)

    override val primaryKey = PrimaryKey(email)
}