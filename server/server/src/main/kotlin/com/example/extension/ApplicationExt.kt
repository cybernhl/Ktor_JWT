package com.example.extension

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.token.config.TokenConfig
import com.example.token.service.JwtTokenService
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.routing.routing

fun Application.configureRouting(tokenService: JwtTokenService, tokenConfig: TokenConfig) {
    routing {


    }
}

fun Application.configureJWTSecurity(tokenConfig: TokenConfig) {
    authentication {
        jwt {
            realm = tokenConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(tokenConfig.secret))
                    .withAudience(tokenConfig.audience)
                    .withIssuer(tokenConfig.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(tokenConfig.audience)) JWTPrincipal(
                    credential.payload
                ) else null
            }
        }
    }
}