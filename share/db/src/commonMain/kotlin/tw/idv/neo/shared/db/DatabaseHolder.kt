package tw.idv.neo.shared.db

import app.cash.sqldelight.db.SqlDriver
import tw.idv.neo.multiplatform.shared.db.KtorDb
import tw.idv.neo.multiplatform.shared.db.KtorQueries
abstract  class DatabaseHolder :IDatabaseDriverFactory{
//     expect fun createDriver(): SqlDriver
    abstract val driver: SqlDriver//this like "expect fun createDriver(): SqlDriver"
    abstract val dbfile: KtorDb
    abstract val dbQueries: KtorQueries
    abstract fun close()
}

typealias DatabaseHelper = DatabaseHolder

typealias DBManager = DatabaseHolder