package tw.idv.neo.shared.db

import tw.idv.neo.multiplatform.shared.db.KtorQueries
import java.util.Properties

class JdbcDbRepo : DatabaseRepo() {
    @Volatile
    private var databaseHolder: DatabaseHolder? = buildDatabaseInstanceIfNeed()
    override val dbQueries: KtorQueries
        get() = databaseHolder?.dbQueries ?: throw PlatformSQLiteThrowable("DB is null")

    override fun buildDatabaseInstanceIfNeed(
        passphrase: CharSequence
    ): DatabaseHolder = synchronized(this) {
        var instance = databaseHolder
        if (instance == null) {
            val properties = Properties()
            if (passphrase.isNotEmpty()) properties["password"] = StringBuilder(passphrase).toString()
            instance = JdbcDatabaseHolder(properties)
            databaseHolder = instance
        }
        return instance
    }

    override fun closeDatabase() {
        synchronized(this) {
            databaseHolder?.close()
            databaseHolder = null
        }
    }
}