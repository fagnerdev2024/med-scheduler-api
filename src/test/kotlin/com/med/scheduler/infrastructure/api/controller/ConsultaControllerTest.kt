package com.med.scheduler.infrastructure.api.controller

import com.med.scheduler.TestFixtures
import com.med.scheduler.application.dto.*
import com.med.scheduler.application.service.ConsultaUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus

class ConsultaControllerTest {
    private val consultaUseCase: ConsultaUseCase = mock()
    private val controller = ConsultaController(consultaUseCase)

    @Test
    fun `deve agendar consulta e retornar 200`() {
        val dados = TestFixtures.dadosAgendamentoConsulta()
        val detalhamento = TestFixtures.consulta().toDetalhamentoDTO()

        whenever(consultaUseCase.agendar(dados)).thenReturn(detalhamento)

        val response = controller.agendar(dados)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(detalhamento, response.body)
        verify(consultaUseCase).agendar(dados)
    }

    @Test
    fun `deve cancelar consulta e retornar 204`() {
        val dados = TestFixtures.dadosCancelamentoConsulta()

        val response = controller.cancelar(dados)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(consultaUseCase).cancelar(dados)
    }

    @Test
    fun `deve listar consultas e retornar 200`() {
        val paginacao = PageRequest.of(0, 10)
        val consulta = TestFixtures.consulta().toDetalhamentoDTO()
        val pagina = PageImpl(listOf(consulta), paginacao, 1)

        whenever(consultaUseCase.listar(paginacao)).thenReturn(pagina)

        val response = controller.listar(paginacao)

        assertEquals(HttpStatus.OK, response.statusCode)
        @Suppress("UNCHECKED_CAST")
        val body = response.body as PageImpl<DadosDetalhamentoConsulta>
        assertEquals(1, body.totalElements)
        assertEquals(consulta.id, body.content.first().id)
    }
}
