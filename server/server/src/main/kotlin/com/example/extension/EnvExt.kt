package com.example.extension

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json

fun Application.CorsEnv() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
}

fun Application.SerializationEnv() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}

//fun Application.CompressionEnv() {
//    gzip()
//}

fun Application.AuthenticationEnv() {
    install(Authentication) {

    }
}