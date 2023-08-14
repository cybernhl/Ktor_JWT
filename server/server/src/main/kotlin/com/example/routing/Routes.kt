package com.example.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.token.config.TokenConfig
import com.example.token.service.JwtTokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.swagger.codegen.v3.generators.html.StaticHtmlCodegen
import tw.idv.neo.shared.data.dto.request.LoginInfo
import tw.idv.neo.shared.data.dto.request.RegisterInfo
import tw.idv.neo.shared.data.dto.respond.AccountInfo
import tw.idv.neo.shared.data.dto.respond.ApiBaseItem
import tw.idv.neo.shared.data.vo.User
import java.util.Date

val customerStorage = mutableListOf<User>() //temp storage
fun Route.signIn(tokenService: JwtTokenService, tokenConfig: TokenConfig){

}

fun Route.login( tokenConfig: TokenConfig){
    post("/login") {
        //FIXME if input request not match Json key Encountered an unknown key !!
        val request = call.receiveNullable<LoginInfo>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        println("Show request $request")
        customerStorage.find { it.username == request.username }?.let {
            // Check username and password
            if (it.password==request.password){
                //TODO gen token
                //
//            val publicKey = jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey
//            val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
//            val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

                val token = JWT.create()
                    .withAudience(tokenConfig.audience)
                    .withIssuer(tokenConfig.issuer)
                    .withClaim("username", request.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + tokenConfig.expiresIn))
                    .sign(Algorithm.HMAC256(tokenConfig.secret))
                val result = ApiBaseItem<AccountInfo>(
                    code = HttpStatusCode.Created.value,
                    message = HttpStatusCode.Created.description,
                    data = AccountInfo(
                        username = request.username,
                        token = token
                    )
                )
                call.respond(HttpStatusCode.OK, result)
            }else{
                println("Show pass error")
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        } ?: run {
            println("Show typo error or account not  error")
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
    }
}

fun Route.signUp(){

}

fun Route.register( tokenConfig: TokenConfig ){
    post("/register") {
        //FIXME how check error input type "form-dat" "x-www-form-urlencoded" "json" ?
        val request = call.receiveNullable<RegisterInfo>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = User(username = request.username, password = request.password)
        customerStorage.add(user)
        val token = JWT.create()
            .withAudience(tokenConfig.audience)
            .withIssuer(tokenConfig.issuer)
            .withClaim("username", request.username)
            .withExpiresAt(Date(System.currentTimeMillis() + tokenConfig.expiresIn))
            .sign(Algorithm.HMAC256(tokenConfig.secret))
        val result = ApiBaseItem<AccountInfo>(
            code = HttpStatusCode.Created.value,
            message = HttpStatusCode.Created.description,
            data = AccountInfo(
                username = user.username,
                token = token
            )
        )
        call.respond(HttpStatusCode.Created, result)
    }
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
fun Route.secretCheck(){
    get("/hello") {
        val principal = call.principal<JWTPrincipal>()
        val username = principal!!.payload.getClaim("username").asString()
        val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
        call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
    }
}
//Swagger
fun Route.APIByswaggerUI(){
    swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
        version = "4.15.5"
    }
}
fun Route.openAPIPage(){
    openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml") {
        codegen = StaticHtmlCodegen()
    }
}
//TEST
fun Route.jsonSerialization(){
    get("/json/kotlinx-serialization") {
        call.respond(mapOf("hello" to "world"))
    }
}
fun Route.findUserById(){
    get("/user/{id}") {
        val id = call.parameters["id"]
        val user = customerStorage.getOrNull(id!!.toInt())
        if (user == null) {
            val result = ApiBaseItem<User>(
                code = HttpStatusCode.Conflict.value,
                message = "用户不存在",
                data = user
            )
            call.respond(HttpStatusCode.OK, result)
            return@get
        } else {
            val result = ApiBaseItem<AccountInfo>(
                code = HttpStatusCode.OK.value,
                message = HttpStatusCode.OK.description,
                data = AccountInfo(
                    username = user.username,
                    token = "fewfewqfe"
                )
            )
            call.respond(HttpStatusCode.OK, result)
        }
    }
}

