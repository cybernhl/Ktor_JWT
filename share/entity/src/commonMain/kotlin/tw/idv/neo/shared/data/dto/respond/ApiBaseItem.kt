package tw.idv.neo.shared.data.dto.respond

import kotlinx.serialization.Serializable

@Serializable
data class ApiBaseItem<T>(
    val code: Int,
    val message: String,
    val data: T?,
)