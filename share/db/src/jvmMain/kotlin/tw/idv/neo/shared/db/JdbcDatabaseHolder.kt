package tw.idv.neo.shared.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.github.aakira.napier.Napier
import tw.idv.neo.multiplatform.shared.db.KtorDb
import tw.idv.neo.multiplatform.shared.db.KtorQueries
import java.sql.SQLException
import java.util.Properties

//actual class DatabaseDriverFactory
class JdbcDatabaseHolder(props: Properties = Properties()) : DatabaseDriverFactory() {
    override fun createDriver(): SqlDriver {
        return driver
    }

    override val driver: SqlDriver = JdbcSqliteDriver(
        url = JdbcSqliteDriver.IN_MEMORY + DatabaseRepo.DB_NAME,
        properties = props
    )

    override val dbfile: KtorDb
        get() = createQueryWrapper(driver)
    override val dbQueries: KtorQueries
        get() = dbfile.ktorQueries

    override fun close() {
        driver.close()
    }

    init {
        if (currentVersion == 0) {
            try {
                KtorDb.Schema.create(driver)
            } catch (sqlException: SQLException) {
                Napier.e("Error creating database", sqlException)
            }
            currentVersion = 1
        } else if (KtorDb.Schema.version > currentVersion) {
            KtorDb.Schema.migrate(driver, currentVersion.toLong(), KtorDb.Schema.version)
        }
    }
}