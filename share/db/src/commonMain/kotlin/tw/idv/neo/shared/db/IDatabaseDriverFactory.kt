package tw.idv.neo.shared.db

import app.cash.sqldelight.db.SqlDriver
interface IDatabaseDriverFactory {
    fun createDriver(): SqlDriver
}