package tw.idv.neo.shared.db

import app.cash.sqldelight.db.SqlDriver
import tw.idv.neo.multiplatform.shared.db.KtorDb
import tw.idv.neo.multiplatform.shared.db.User
import tw.idv.neo.shared.db.adapter.DateAdapter
import tw.idv.neo.shared.db.adapter.LongIntAdapter

fun createQueryWrapper(sqlDriver: SqlDriver): KtorDb {
    val idAdapter = LongIntAdapter()
    val dateColumnAdapter = DateAdapter()
    val userAdapter= User.Adapter(idAdapter,dateColumnAdapter)
    return KtorDb(driver = sqlDriver,userAdapter )
}