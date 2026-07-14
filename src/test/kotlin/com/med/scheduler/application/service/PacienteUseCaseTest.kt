package com.med.scheduler.application.service

import com.med.scheduler.TestFixtures
import com.med.scheduler.application.dto.DadosAtualizacaoPaciente
import com.med.scheduler.domain.repository.PacienteRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

class PacienteUseCaseTest {
    private val pacienteRepository: PacienteRepository = mock()
    private val pacienteUseCase = PacienteUseCase(pacienteRepository)

    @BeforeEach
    fun setUp() {
        clearInvocations(pacienteRepository)
    }

    @Test
    fun `deve cadastrar paciente e retornar dados de detalhamento`() {
        val dadosCadastro = TestFixtures.dadosCadastroPaciente()
        val pacienteSalvo = TestFixtures.paciente()

        whenever(pacienteRepository.save(any())).thenReturn(pacienteSalvo)

        val resultado = pacienteUseCase.cadastrar(dadosCadastro)

        assertNotNull(resultado)
        assertEquals(pacienteSalvo.id, resultado.id)
        assertEquals(pacienteSalvo.nome, resultado.nome)
        assertEquals(pacienteSalvo.email, resultado.email)
        assertEquals(pacienteSalvo.cpf, resultado.cpf)
        assertTrue(resultado.ativo)

        verify(pacienteRepository).save(any())
    }

    @Test
    fun `deve listar pacientes ativos com paginacao`() {
        val paginacao = PageRequest.of(0, 10)
        val paciente = TestFixtures.paciente()
        val pagina = PageImpl(listOf(paciente), paginacao, 1)

        whenever(pacienteRepository.findAllByAtivoTrue(paginacao)).thenReturn(pagina)

        val resultado = pacienteUseCase.listar(paginacao)

        assertEquals(1, resultado.totalElements)
        assertEquals(paciente.id, resultado.content.first().id)

        verify(pacienteRepository).findAllByAtivoTrue(paginacao)
    }

    @Test
    fun `deve lancar excecao quando tamanho da pagina exceder o limite`() {
        val paginacao = PageRequest.of(0, 101)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                pacienteUseCase.listar(paginacao)
            }

        assertEquals("O tamanho da página não pode exceder 100 registros.", exception.message)
        verifyNoInteractions(pacienteRepository)
    }

    @Test
    fun `deve atualizar paciente existente`() {
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val dadosAtualizacao =
            DadosAtualizacaoPaciente(
                id = pacienteId,
                nome = "Paciente Atualizado",
                telefone = "11888888888",
                endereco = TestFixtures.dadosEndereco(logradouro = "Rua Nova"),
            )

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(pacienteRepository.save(paciente)).thenReturn(paciente)

        val resultado = pacienteUseCase.atualizar(dadosAtualizacao)

        assertEquals("Paciente Atualizado", resultado.nome)
        assertEquals("11888888888", resultado.telefone)

        verify(pacienteRepository).findById(pacienteId)
        verify(pacienteRepository).save(paciente)
    }

    @Test
    fun `deve atualizar paciente mantendo endereco`() {
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val enderecoOriginal = paciente.endereco
        val dadosAtualizacao =
            DadosAtualizacaoPaciente(
                id = pacienteId,
                nome = "Paciente Atualizado",
                telefone = "11888888888",
                endereco = null,
            )

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(pacienteRepository.save(paciente)).thenReturn(paciente)

        val resultado = pacienteUseCase.atualizar(dadosAtualizacao)

        assertEquals("Paciente Atualizado", resultado.nome)
        assertEquals(enderecoOriginal.logradouro, resultado.endereco.logradouro)
    }

    @Test
    fun `deve lancar excecao ao atualizar paciente inexistente`() {
        val dadosAtualizacao =
            DadosAtualizacaoPaciente(
                id = 999L,
                nome = "Paciente Inexistente",
                telefone = null,
                endereco = null,
            )

        whenever(pacienteRepository.findById(dadosAtualizacao.id)).thenReturn(null)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                pacienteUseCase.atualizar(dadosAtualizacao)
            }

        assertEquals("Paciente com ID 999 não encontrado.", exception.message)
    }

    @Test
    fun `deve excluir paciente logicamente`() {
        val paciente = TestFixtures.paciente(ativo = true)
        val pacienteId = paciente.id!!

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)

        pacienteUseCase.excluir(pacienteId)

        assertFalse(paciente.ativo)
        verify(pacienteRepository).save(paciente)
    }

    @Test
    fun `deve lancar excecao ao excluir paciente inativo`() {
        val paciente = TestFixtures.paciente(ativo = false)
        val pacienteId = paciente.id!!

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                pacienteUseCase.excluir(pacienteId)
            }

        assertEquals("Paciente com ID $pacienteId já está inativo.", exception.message)
        verify(pacienteRepository, never()).save(any())
    }

    @Test
    fun `deve detalhar paciente ativo`() {
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)

        val resultado = pacienteUseCase.detalhar(pacienteId)

        assertEquals(paciente.id, resultado.id)
        assertEquals(paciente.nome, resultado.nome)
    }

    @Test
    fun `deve lancar excecao ao detalhar paciente inativo`() {
        val paciente = TestFixtures.paciente(ativo = false)
        val pacienteId = paciente.id!!

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                pacienteUseCase.detalhar(pacienteId)
            }

        assertEquals("Paciente com ID $pacienteId está inativo.", exception.message)
    }

    @Test
    fun `deve retornar pagina vazia ao listar sem registros`() {
        val paginacao = PageRequest.of(0, 10)

        whenever(pacienteRepository.findAllByAtivoTrue(paginacao)).thenReturn(Page.empty())

        val resultado = pacienteUseCase.listar(paginacao)

        assertTrue(resultado.isEmpty)
        verify(pacienteRepository).findAllByAtivoTrue(paginacao)
    }

    @Test
    fun `deve lancar excecao ao excluir paciente inexistente`() {
        whenever(pacienteRepository.findById(999L)).thenReturn(null)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                pacienteUseCase.excluir(999L)
            }

        assertEquals("Paciente com ID 999 não encontrado.", exception.message)
    }

    @Test
    fun `deve lancar excecao ao detalhar paciente inexistente`() {
        whenever(pacienteRepository.findById(999L)).thenReturn(null)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                pacienteUseCase.detalhar(999L)
            }

        assertEquals("Paciente com ID 999 não encontrado.", exception.message)
    }
}
