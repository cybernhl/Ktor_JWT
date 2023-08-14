package com.example.extension

import com.example.token.config.TokenConfig
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import kotlinx.serialization.json.Json
fun Application.JsonSerializationEnv() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            //https://stackoverflow.com/questions/66742155/ignoreunknownkeys-for-one-type-only-with-kotlinx-and-ktor
            ignoreUnknownKeys = true
        })
    }
}
fun Application.CorsEnv() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
}

//fun Application.CompressionEnv() {
//    gzip()
//}


fun Application.SecurityAuthenticationJWTEnv(tokenConfig: TokenConfig) {
    install(Authentication) { // "install(Authentication)" is the same "authentication"
        jwt() {//"auth-jwt"
            realm = tokenConfig.realm
            val jwtverifier = com.auth0.jwt.JWT
                .require(com.auth0.jwt.algorithms.Algorithm.HMAC256(tokenConfig.secret))
                .withAudience(tokenConfig.audience)
                .withIssuer(tokenConfig.issuer)
                .build()
            verifier(
                jwtverifier
            )
//            verifier(jwkProvider, issuer) {
//                acceptLeeway(3)
//            }
            validate { credential ->
                if (credential.payload.audience.contains(tokenConfig.audience)) JWTPrincipal(
                    credential.payload
                ) else if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else null
            }
//            challenge { defaultScheme, realm ->
//                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
//            }
        }
    }
}