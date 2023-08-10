package tw.idv.neo.shared.data.request

import kotlinx.serialization.*

@Serializable
data class AuthUser(
    val username: String,
    val password: String
)