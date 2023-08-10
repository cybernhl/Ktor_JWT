package com.example

data class TokenConfig(
    val audience: String,
    val issuer: String,
    val expiresIn: Long,
    val secret: String,
    val realm:String
)