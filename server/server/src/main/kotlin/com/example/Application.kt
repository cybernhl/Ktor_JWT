package com.example

import io.ktor.server.application.*
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import tw.idv.neo.shared.data.vo.User
import com.auth0.jwk.*
import com.example.extension.CorsEnv
import com.example.extension.JsonSerializationEnv
import com.example.extension.SecurityAuthenticationJWTEnv
import com.example.extension.configureRouting
import com.example.token.config.TokenConfig
import tw.idv.neo.shared.db.Repository
import java.util.concurrent.*

//fun main() {
//    // embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
//    //     .start(wait = true)
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)
//}
//
//fun Application.module() {
fun Application.main() {
    val customerStorage =Repository.customerStorage
    //gen fake data
   customerStorage.addAll(
        arrayOf(
            tw.idv.neo.shared.db.User(username = "Jane", password = "fake1"),
            tw.idv.neo.shared.db.User(username = "John", password = "fake2")
        )
    )

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

    val jwkProvider = JwkProviderBuilder(tokenConfig.issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()
    val jwtverifier = JWT
        .require(Algorithm.HMAC256(tokenConfig.secret))
        .withAudience(tokenConfig.audience)
        .withIssuer(tokenConfig.issuer)

    configureRouting(tokenConfig)
}
