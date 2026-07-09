package com.med.scheduler.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class TokenService(
    @Value("\${api.security.token.secret}")
    private val secret: String
) {
    fun gerarToken(userDetails: UserDetails): String {
        val algorithm = Algorithm.HMAC256(secret)
        return JWT.create()
            .withIssuer("API Voll Med")
            .withSubject(userDetails.username)
            .withExpiresAt(LocalDateTime.now().plusHours(2).atZone(ZoneId.of("UTC")).toInstant())
            .sign(algorithm)
    }
}