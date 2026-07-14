package com.med.scheduler.infrastructure.api.controller

import com.med.scheduler.application.dto.DadosAutenticacao
import com.med.scheduler.application.dto.DadosTokenJWT
import com.med.scheduler.application.service.AutenticacaoUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.http.HttpStatus

class AutenticacaoControllerTest {
    private val autenticacaoUseCase: AutenticacaoUseCase = mock()
    private val controller = AutenticacaoController(autenticacaoUseCase)

    @Test
    fun `deve efetuar login e retornar token`() {
        val dados = DadosAutenticacao(login = "usuario@teste.com", senha = "senha123")
        val token = DadosTokenJWT("token-jwt-123")

        whenever(autenticacaoUseCase.autenticar(dados)).thenReturn(token)

        val response = controller.efetuarLogin(dados)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(token, response.body)
        verify(autenticacaoUseCase).autenticar(dados)
    }
}
