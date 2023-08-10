package tw.idv.neo.shared.data.respond

import kotlinx.serialization.*

@Serializable
data class AuthUser(
    val username: String,
    val token: String
)