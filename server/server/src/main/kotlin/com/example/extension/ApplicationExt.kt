package com.example.extension

import com.example.routing.APIByswaggerUI
import com.example.routing.findUserById
import com.example.routing.login
import com.example.routing.openAPIPage
import com.example.routing.register
import com.example.routing.secret
import com.example.routing.signIn
import com.example.routing.signUp
import com.example.token.config.TokenConfig
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.routing.routing

fun Application.configureRouting(tokenConfig: TokenConfig) {
    routing {
        signUp(tokenConfig)
        register(tokenConfig)
        login(tokenConfig)
        signIn(  tokenConfig)

        authenticate {//"auth-jwt"
            secret()
//            secretCheck()
        }
        APIByswaggerUI()
        openAPIPage()
        //TEST
        findUserById()
    }
}

