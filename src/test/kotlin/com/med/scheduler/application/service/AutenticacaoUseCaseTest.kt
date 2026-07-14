package com.med.scheduler.application.service

import com.med.scheduler.application.dto.DadosAutenticacao
import com.med.scheduler.domain.service.TokenService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class AutenticacaoUseCaseTest {
    private val authenticationManager: AuthenticationManager = mock()
    private val tokenService: TokenService = mock()
    private val autenticacaoUseCase = AutenticacaoUseCase(authenticationManager, tokenService)

    @Test
    fun `deve autenticar usuario e retornar token JWT`() {
        val dadosAutenticacao =
            DadosAutenticacao(
                login = "usuario@teste.com",
                senha = "senha123",
            )

        val userDetails =
            User(
                dadosAutenticacao.login,
                dadosAutenticacao.senha,
                listOf(SimpleGrantedAuthority("ROLE_USER")),
            )

        val authentication =
            UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities,
            )

        whenever(
            authenticationManager.authenticate(
                eq(UsernamePasswordAuthenticationToken(dadosAutenticacao.login, dadosAutenticacao.senha)),
            ),
        ).thenReturn(authentication)

        whenever(tokenService.gerarToken(userDetails)).thenReturn("token-jwt-123")

        val resultado = autenticacaoUseCase.autenticar(dadosAutenticacao)

        assertEquals("token-jwt-123", resultado.token)

        verify(authenticationManager).authenticate(
            eq(UsernamePasswordAuthenticationToken(dadosAutenticacao.login, dadosAutenticacao.senha)),
        )
        verify(tokenService).gerarToken(userDetails)
    }
}
