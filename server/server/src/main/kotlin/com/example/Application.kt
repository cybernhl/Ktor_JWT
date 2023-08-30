package com.example

import io.ktor.server.application.*
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import com.auth0.jwk.*
import com.example.extension.CallLogMonitoringEnv
import com.example.extension.CorsEnv
import com.example.extension.JsonSerializationEnv
import com.example.extension.SecurityAuthenticationJWTEnv
import com.example.extension.configureRouting
import com.example.token.config.TokenConfig
import tw.idv.neo.shared.db.DatabaseRepo
import java.util.concurrent.*
import kotlinx.datetime.LocalDateTime

//fun main() {
//    // embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
//    //     .start(wait = true)
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)
//}
//
//fun Application.module() {
fun Application.main() {
    val customerStorage =DatabaseRepo.customerStorage
    //gen fake data
   customerStorage.addAll(
        arrayOf(
            tw.idv.neo.multiplatform.shared.db.User(id=0,userid="wqfwe",name = "Jane", password = "fake1", device_id = "fasdfsd",token="wefwe", dateCreated =LocalDateTime(2018, 1, 2, 3, 4))  ,
            tw.idv.neo.multiplatform.shared.db.User(id=1,userid="vczxvxc",name = "DDD", password = "fake2", device_id = "sdaafsdffsd",token="wefwegfdgsfdg", dateCreated =LocalDateTime(2018, 1, 4, 3, 4))  ,
        )
    )
    CallLogMonitoringEnv()
    JsonSerializationEnv()
    CorsEnv()
//    val privateKeyString = environment.config.property("jwt.privateKey").getString()
    val tokenConfig = TokenConfig(
        audience = environment.config.property("jwt.audience").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        expiresIn = 60000L,//Date(System.currentTimeMillis() + 60000)
        secret = environment.config.property("jwt.secret").getString(),
        realm = environment.config.property("jwt.realm").getString()
    )
    SecurityAuthenticationJWTEnv(tokenConfig)

//    val jwkProvider = JwkProviderBuilder(tokenConfig.issuer)
//        .cached(10, 24, TimeUnit.HOURS)
//        .rateLimited(10, 1, TimeUnit.MINUTES)
//        .build()
//    val jwtverifier = JWT
//        .require(Algorithm.HMAC256(tokenConfig.secret))
//        .withAudience(tokenConfig.audience)
//        .withIssuer(tokenConfig.issuer)

    configureRouting(tokenConfig)
}
