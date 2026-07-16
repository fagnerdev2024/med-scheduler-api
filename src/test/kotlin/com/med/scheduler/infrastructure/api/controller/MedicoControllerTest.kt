package com.med.scheduler.infrastructure.api.controller

import com.med.scheduler.TestFixtures
import com.med.scheduler.application.dto.*
import com.med.scheduler.application.service.MedicoUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder

class MedicoControllerTest {
    private val medicoUseCase: MedicoUseCase = mock()
    private val controller = MedicoController(medicoUseCase)

    @Test
    fun `deve cadastrar medico e retornar 201 com location`() {
        val dados = TestFixtures.dadosCadastroMedico()
        val detalhamento = TestFixtures.medico().toDetalhamentoDTO()

        whenever(medicoUseCase.cadastrar(dados)).thenReturn(detalhamento)

        val response = controller.cadastrar(dados, UriComponentsBuilder.newInstance())

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(detalhamento, response.body)
        assertEquals("/medicos/${detalhamento.id}", response.headers.location?.path)
        verify(medicoUseCase).cadastrar(dados)
    }

    @Test
    fun `deve listar medicos quando houver registros`() {
        val paginacao = PageRequest.of(0, 10)
        val medico = TestFixtures.medico().toListagemDTO()
        val pagina = PageImpl(listOf(medico), paginacao, 1)

        whenever(medicoUseCase.listar(paginacao)).thenReturn(pagina)

        val response = controller.listar(paginacao)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body is Page<*>)
        @Suppress("UNCHECKED_CAST")
        val body = response.body as Page<DadosListagemMedico>
        assertEquals(1, body.totalElements)
        assertEquals(medico.id, body.content.first().id)
    }

    @Test
    fun `deve retornar mensagem quando nao houver medicos`() {
        val paginacao = PageRequest.of(0, 10)

        whenever(medicoUseCase.listar(paginacao)).thenReturn(Page.empty())

        val response = controller.listar(paginacao)

        assertEquals(HttpStatus.OK, response.statusCode)
        @Suppress("UNCHECKED_CAST")
        val body = response.body as Map<String, String>
        assertEquals("Nenhum médico ativo encontrado", body["message"])
    }

    @Test
    fun `deve atualizar medico e retornar 200`() {
        val id = 1L
        val request = TestFixtures.dadosAtualizacaoMedicoRequest()
        val dados = request.toUseCase(id)
        val detalhamento = TestFixtures.medico().toDetalhamentoDTO()

        whenever(medicoUseCase.atualizar(dados)).thenReturn(detalhamento)

        val response = controller.atualizar(id, request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(detalhamento, response.body)
        verify(medicoUseCase).atualizar(dados)
    }

    @Test
    fun `deve excluir medico e retornar 204`() {
        val id = 1L

        val response = controller.excluir(id)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(medicoUseCase).excluir(id)
    }

    @Test
    fun `deve detalhar medico e retornar 200`() {
        val id = 1L
        val detalhamento = TestFixtures.medico().toDetalhamentoDTO()

        whenever(medicoUseCase.detalhar(id)).thenReturn(detalhamento)

        val response = controller.detalhar(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(detalhamento, response.body)
        verify(medicoUseCase).detalhar(id)
    }
}
