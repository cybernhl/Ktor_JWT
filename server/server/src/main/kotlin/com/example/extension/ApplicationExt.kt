package com.example.extension

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.routing.APIByswaggerUI
import com.example.routing.findUserById
import com.example.routing.login
import com.example.routing.openAPIPage
import com.example.routing.register
import com.example.routing.secret
import com.example.routing.secretCheck
import com.example.token.config.TokenConfig
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.routing.routing

fun Application.configureRouting(tokenConfig: TokenConfig) {
    routing {
//        signUp()
        register(tokenConfig)
        login(tokenConfig)
//        signIn(  tokenService, tokenConfig)

        authenticate {
            secret()
        }
        authenticate("auth-jwt") {
            secretCheck()
        }
        APIByswaggerUI()
        openAPIPage()
        //TEST
        findUserById()
    }
}

fun Application.configureSecurityAuthJWT(tokenConfig: TokenConfig) {
    authentication {
        jwt {
            realm = tokenConfig.realm
            val jwtverifier = JWT
                .require(Algorithm.HMAC256(tokenConfig.secret))
                .withAudience(tokenConfig.audience)
                .withIssuer(tokenConfig.issuer)
                .build()
            verifier(
                jwtverifier
            )
            validate { credential ->
                if (credential.payload.audience.contains(tokenConfig.audience)) JWTPrincipal(
                    credential.payload
                ) else null
            }
        }
    }
}