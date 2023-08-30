package tw.idv.neo.shared.db.usecase

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import tw.idv.neo.multiplatform.shared.db.User
import tw.idv.neo.shared.db.DatabaseRepo
import tw.idv.neo.shared.db.createLocalDateTime

//like DAO
class UserCase(private val dbRepo: DatabaseRepo) {
    private val queries = dbRepo.dbQueries
    fun getAllUsers(): Flow<List<User>> =
        queries.getAllUsers().asFlow().mapToList(Dispatchers.IO).distinctUntilChanged()

    //FIXME merge to  "kotlin find with filter"

    fun findUserByIndex(index: Int): Flow<User> =
//        queries.getUserByIndex(index).executeAsOneOrNull()
        queries.getUserByIndex(index).asFlow().mapToOne(Dispatchers.IO).distinctUntilChanged()

    fun findUserByName(name: String): Flow<User> =
        queries.findUserByName(name).asFlow().mapToOne(Dispatchers.IO).distinctUntilChanged()

    fun findUserByToken(token: String): Flow<User> =
        queries.findUserByName(token).asFlow().mapToOne(Dispatchers.IO).distinctUntilChanged()

    fun findUserByUserId(user_id: String): Flow<User> =
        queries.findUserByName(user_id).asFlow().mapToOne(Dispatchers.IO).distinctUntilChanged()

    fun findUserByDeviceId(deviceid: String): Flow<User> =
        queries.findUserByName(deviceid).asFlow().mapToOne(Dispatchers.IO).distinctUntilChanged()

    @Throws(Exception::class)
    suspend fun insertUser(userid: String, name: String, password: String, device_id: String, token: String): Long {
        try {
            val localDateTime = createLocalDateTime()
            return queries.insertUser(userid, name, password, device_id, token, localDateTime) .executeAsOne()
        } catch (cause: Throwable) {
            throw RuntimeException("Error : create user ", cause)
        }
    }
}