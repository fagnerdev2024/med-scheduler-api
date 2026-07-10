package com.med.scheduler.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.med.scheduler.domain.service.TokenService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class TokenService(
    @Value("\${api.security.token.secret}")
    private val secret: String
) : TokenService {

    companion object {
        private const val ISSUER = "API Med Scheduler"
    }

    override fun gerarToken(userDetails: UserDetails): String {
        try {
            val algorithm = Algorithm.HMAC256(secret)
            return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(userDetails.username)
                .withExpiresAt(LocalDateTime.now().plusHours(2).atZone(ZoneId.of("UTC")).toInstant())
                .sign(algorithm)
        } catch (exception: JWTCreationException) {
            throw RuntimeException("Erro ao gerar token JWT", exception)
        }
    }

    override fun getSubject(token: String): String? {
        return try {
            val algorithm = Algorithm.HMAC256(secret)
            JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .subject
        } catch (exception: JWTVerificationException) {
            null
        }
    }
}
