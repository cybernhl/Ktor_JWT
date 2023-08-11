package com.example.routing

import com.example.token.config.TokenConfig
import com.example.token.service.JwtTokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.signIn(tokenService: JwtTokenService, tokenConfig: TokenConfig){

}

fun Route.signUp(){

}

fun Route.secret(){
    get("secret"){
        val userId = call.principal<JWTPrincipal>()?.getClaim("userId", String::class)

        call.respond(
            HttpStatusCode.OK,
            message = userId!!
        )
    }
}