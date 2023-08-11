package com.example.server

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.nio.charset.Charset
import java.util.*
import java.util.logging.Logger
object ServerFactory {
    private val logger = Logger.getLogger("KtorServer")
    fun getServer(port: Int): ApplicationEngine {
        return embeddedServer(Netty, port, watchPaths = emptyList()) {

        }
    }
}