package com.example.token.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.token.claim.TokenClaim
import com.example.token.config.TokenConfig
import java.util.Date

class JwtTokenService {
    fun generate(config: TokenConfig, claims: List<TokenClaim>): String{
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach {
            token = token.withClaim(it.key, it.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}