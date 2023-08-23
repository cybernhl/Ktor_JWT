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
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.auth.authenticate
import io.ktor.server.html.respondHtml
import kotlinx.html.*
//import kotlinx.html.body
//import kotlinx.html.h1
//import kotlinx.html.head
//import kotlinx.html.p
//import kotlinx.html.title


//Ref : https://stackoverflow.com/questions/75111526/how-to-set-authentication-in-kotlin-ktor-routing-for-every-route-and-pass-a-the
//Ref : https://stackoverflow.com/questions/52544123/kotlin-ktor-how-to-respond-text-as-html
//Ref : https://github.com/GoogleContainerTools/jib/blob/master/examples/ktor/src/main/kotlin/example/ktor/App.kt
fun Application.configureRouting(tokenConfig: TokenConfig) {
    routing {
        get("/") {
            val general_name = "Ktor"
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    title { +"${general_name}Server Info" }
                }
                body {
                    h1 {
                        +"${general_name}Server"
                    }
                    a {
                        href = "/register"
                        +"注册"
                    }
                    p {
                        +"This server provides OUIs (Organizationally Unique Identifiers) that have been assigned to a manufacturer by IEEE."
                        br
                        +"You can lookup OUIs using the manufacturer name and also discover the manufacturer for a given OUI."
                        br
                        +"See the GitHub site for more information: https://github.com/bwixted/ktorserver"
                    }
                    //FormEncType.applicationXWwwFormUrlEncoded
                    form("/user/login", method = FormMethod.post, encType = FormEncType.multipartFormData ) {
                        input {
                            type = InputType.text
                            name = "username"
                        }
                        br
                        p {
                            +"Password:"
                            passwordInput(name = "password")
                        }
                        br
                        input {
                            type = InputType.submit
                            value = "login"
                        }
                    }
                }
            }
        }
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

