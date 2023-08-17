package tw.idv.neo.shared.db

import app.cash.sqldelight.db.SqlDriver
import tw.idv.neo.multiplatform.shared.db.KtorDb
import tw.idv.neo.multiplatform.shared.db.KtorQueries
//or expect class
abstract  class DatabaseHolder :IDatabaseDriverFactory{
   protected var currentVersion: Int
        get() {
            val queryResult = driver.execute(null, "PRAGMA user_version;", 0, null)
            val ver: Long = queryResult.value
            return ver.toInt()
        }
        set(value) {
            driver.execute(null, "PRAGMA user_version = $value;", 0, null)
        }
    abstract val driver: SqlDriver//this like "expect fun createDriver(): SqlDriver"
    abstract val dbfile: KtorDb
    abstract val dbQueries: KtorQueries
    abstract fun close()
}

typealias DatabaseHelper = DatabaseHolder

typealias DBManager = DatabaseHolder

typealias DatabaseDriverFactory = DatabaseHolder
