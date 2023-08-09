package com.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.util.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.swagger.codegen.v3.generators.html.*
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.auth.*
import tw.idv.neo.shared.data.User

import com.auth0.jwk.*
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.concurrent.*

fun Application.main() {
    val customerStorage = mutableListOf<User>()
    customerStorage.addAll(
        arrayOf(
            User(1, "Jane", "Smith"),
            User(2, "John", "Smith")
        )
    )

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
//    val privateKeyString = environment.config.property("jwt.privateKey").getString()
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()
    val jwkProvider = JwkProviderBuilder(issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()
    val jwtverifier=JWT
        .require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()
    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(jwtverifier)
//            verifier(jwkProvider, issuer) {
//                acceptLeeway(3)
//            }
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
    routing {
        get("/user/{id}") {
            val id = call.parameters["id"]
            val customer: User = customerStorage.find { it.id == id!!.toInt() }!!
            call.respond(customer)
        }

        post("/register") {
            val customer = call.receive<User>()
            customerStorage.add(customer)
            call.respondText("User Customer stored correctly", status = HttpStatusCode.Created)
        }

        post("/login") {
            val user = call.receive<User>()
            // Check username and password
            //
//            val publicKey = jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey
//            val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
//            val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", user.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(secret))
            call.respond(hashMapOf("token" to token,"status" to HttpStatusCode.Created))
        }

        authenticate("auth-jwt") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }

        //swagger

        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }
        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml") {
            codegen = StaticHtmlCodegen()
        }
    }
}
