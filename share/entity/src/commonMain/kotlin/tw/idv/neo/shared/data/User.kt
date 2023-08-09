package tw.idv.neo.shared.data

import kotlinx.serialization.*

@Serializable
data class User(val id: Int, val username: String, val lastName: String="Smith", val password: String="")