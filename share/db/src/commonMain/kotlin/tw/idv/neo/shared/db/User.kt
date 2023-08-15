package tw.idv.neo.shared.db

import kotlinx.serialization.Serializable

//DB class
@Serializable
data class User(
    val id:Int=0,
    val userId:Int=0,
    val username: String,
    val password: String="",
    var token: String=""
)