package com.med.scheduler.infrastructure.api.controller

import com.med.scheduler.TestFixtures
import com.med.scheduler.application.dto.*
import com.med.scheduler.application.service.PacienteUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder

class PacienteControllerTest {
    private val pacienteUseCase: PacienteUseCase = mock()
    private val controller = PacienteController(pacienteUseCase)

    @Test
    fun `deve cadastrar paciente e retornar 201 com location`() {
        val dados = TestFixtures.dadosCadastroPaciente()
        val detalhamento = TestFixtures.paciente().toDetalhamentoDTO()

        whenever(pacienteUseCase.cadastrar(dados)).thenReturn(detalhamento)

        val response = controller.cadastrar(dados, UriComponentsBuilder.newInstance())

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(detalhamento, response.body)
        assertEquals("/pacientes/${detalhamento.id}", response.headers.location?.path)
        verify(pacienteUseCase).cadastrar(dados)
    }

    @Test
    fun `deve listar pacientes quando houver registros`() {
        val paginacao = PageRequest.of(0, 10)
        val paciente = TestFixtures.paciente().toListagemDTO()
        val pagina = PageImpl(listOf(paciente), paginacao, 1)

        whenever(pacienteUseCase.listar(paginacao)).thenReturn(pagina)

        val response = controller.listar(paginacao)

        assertEquals(HttpStatus.OK, response.statusCode)
        @Suppress("UNCHECKED_CAST")
        val body = response.body as Page<DadosListagemPaciente>
        assertEquals(1, body.totalElements)
        assertEquals(paciente.id, body.content.first().id)
    }

    @Test
    fun `deve retornar mensagem quando nao houver pacientes`() {
        val paginacao = PageRequest.of(0, 10)

        whenever(pacienteUseCase.listar(paginacao)).thenReturn(Page.empty())

        val response = controller.listar(paginacao)

        assertEquals(HttpStatus.OK, response.statusCode)
        @Suppress("UNCHECKED_CAST")
        val body = response.body as Map<String, String>
        assertEquals("Nenhum paciente ativo encontrado", body["message"])
    }

    @Test
    fun `deve atualizar paciente e retornar 200`() {
        val dados = TestFixtures.dadosAtualizacaoPaciente()
        val detalhamento = TestFixtures.paciente().toDetalhamentoDTO()

        whenever(pacienteUseCase.atualizar(dados)).thenReturn(detalhamento)

        val response = controller.atualizar(dados)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(detalhamento, response.body)
        verify(pacienteUseCase).atualizar(dados)
    }

    @Test
    fun `deve excluir paciente e retornar 204`() {
        val id = 1L

        val response = controller.excluir(id)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(pacienteUseCase).excluir(id)
    }

    @Test
    fun `deve detalhar paciente e retornar 200`() {
        val id = 1L
        val detalhamento = TestFixtures.paciente().toDetalhamentoDTO()

        whenever(pacienteUseCase.detalhar(id)).thenReturn(detalhamento)

        val response = controller.detalhar(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(detalhamento, response.body)
        verify(pacienteUseCase).detalhar(id)
    }
}
