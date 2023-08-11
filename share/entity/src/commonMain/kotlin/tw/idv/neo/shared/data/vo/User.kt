package tw.idv.neo.shared.data.vo

import kotlinx.serialization.*

@Serializable
data class User(
    val userId:Int=0,
    val username: String,
    val password: String
)