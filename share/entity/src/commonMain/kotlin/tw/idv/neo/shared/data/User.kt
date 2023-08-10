package tw.idv.neo.shared.data

import kotlinx.serialization.*

@Serializable
data class User(
    val username: String,
    val password: String
)