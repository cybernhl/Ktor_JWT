package tw.idv.neo.shared.data.dto.respond

import kotlinx.serialization.*

@Serializable
data class AccountInfo(
    val username: String,
    val token: String
)