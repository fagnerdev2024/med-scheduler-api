package com.med.scheduler.infrastructure.security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class TokenServiceTest {
    private val tokenService = TokenService("minha-chave-secreta")

    @Test
    fun `deve gerar token JWT e extrair subject`() {
        val user =
            User(
                "usuario@teste.com",
                "senha123",
                listOf(SimpleGrantedAuthority("ROLE_USER")),
            )

        val token = tokenService.gerarToken(user)

        assertNotNull(token)
        assertTrue(token.isNotBlank())
        assertEquals("usuario@teste.com", tokenService.getSubject(token))
    }

    @Test
    fun `deve retornar null para token invalido`() {
        val resultado = tokenService.getSubject("token-invalido")

        assertNull(resultado)
    }

    @Test
    fun `deve retornar null para token com assinatura diferente`() {
        val outroTokenService = TokenService("outra-chave")
        val user =
            User(
                "usuario@teste.com",
                "senha123",
                listOf(SimpleGrantedAuthority("ROLE_USER")),
            )

        val token = outroTokenService.gerarToken(user)

        assertNull(tokenService.getSubject(token))
    }
}
