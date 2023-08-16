package tw.idv.neo.shared.db

import tw.idv.neo.multiplatform.shared.db.KtorQueries

abstract class DatabaseRepo {
    companion object {
        val customerStorage = mutableListOf<tw.idv.neo.multiplatform.shared.db.User>()
        const val DB_NAME = "User.db"

        fun copyCharArray(input: CharArray): CharArray = CharArray(input.size, input::get)
    }
     //temp storage will replace to Database
    abstract val dbQueries: KtorQueries
    abstract fun closeDatabase()


}