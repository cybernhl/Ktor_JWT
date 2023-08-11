package tw.idv.neo.shared.data.dto.request

import kotlinx.serialization.*

@Serializable
data class RegisterInfo(
    val username: String,
    val password: String
)