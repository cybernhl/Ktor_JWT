package tw.idv.neo.shared.data.dto.request

import kotlinx.serialization.*

@Serializable
data class UserInfo(
    val username: String,
    val password: String
)

//Ref : https://juejin.cn/post/6844904184911757325
typealias RegisterInfo = UserInfo
typealias LoginInfo = UserInfo