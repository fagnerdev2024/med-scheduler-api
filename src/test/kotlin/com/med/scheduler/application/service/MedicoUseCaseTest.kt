package com.med.scheduler.application.service

import com.med.scheduler.TestFixtures
import com.med.scheduler.application.dto.DadosAtualizacaoMedico
import com.med.scheduler.domain.repository.MedicoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

class MedicoUseCaseTest {

    private val medicoRepository: MedicoRepository = mock()
    private val medicoUseCase = MedicoUseCase(medicoRepository)

    @BeforeEach
    fun setUp() {
        clearInvocations(medicoRepository)
    }

    @Test
    fun `deve cadastrar medico e retornar dados de detalhamento`() {
        val dadosCadastro = TestFixtures.dadosCadastroMedico()
        val medicoSalvo = TestFixtures.medico()

        whenever(medicoRepository.save(any())).thenReturn(medicoSalvo)

        val resultado = medicoUseCase.cadastrar(dadosCadastro)

        assertNotNull(resultado)
        assertEquals(medicoSalvo.id, resultado.id)
        assertEquals(medicoSalvo.nome, resultado.nome)
        assertEquals(medicoSalvo.email, resultado.email)
        assertEquals(medicoSalvo.crm, resultado.crm)
        assertEquals(medicoSalvo.especialidade, resultado.especialidade)
        assertTrue(resultado.ativo)

        verify(medicoRepository).save(any())
    }

    @Test
    fun `deve listar medicos ativos com paginacao`() {
        val paginacao = PageRequest.of(0, 10)
        val medico = TestFixtures.medico()
        val pagina = PageImpl(listOf(medico), paginacao, 1)

        whenever(medicoRepository.findAllByAtivoTrue(paginacao)).thenReturn(pagina)

        val resultado = medicoUseCase.listar(paginacao)

        assertEquals(1, resultado.totalElements)
        assertEquals(medico.id, resultado.content.first().id)

        verify(medicoRepository).findAllByAtivoTrue(paginacao)
    }

    @Test
    fun `deve lancar excecao quando tamanho da pagina exceder o limite`() {
        val paginacao = PageRequest.of(0, 101)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            medicoUseCase.listar(paginacao)
        }

        assertEquals("O tamanho da página não pode exceder 100 registros.", exception.message)
        verifyNoInteractions(medicoRepository)
    }

    @Test
    fun `deve atualizar medico existente`() {
        val medico = TestFixtures.medico()
        val medicoId = medico.id!!
        val dadosAtualizacao = DadosAtualizacaoMedico(
            id = medicoId,
            nome = "Dr. Atualizado",
            telefone = "11888888888",
            endereco = TestFixtures.dadosEndereco(logradouro = "Rua Nova")
        )

        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)
        whenever(medicoRepository.save(medico)).thenReturn(medico)

        val resultado = medicoUseCase.atualizar(dadosAtualizacao)

        assertEquals("Dr. Atualizado", resultado.nome)
        assertEquals("11888888888", resultado.telefone)

        verify(medicoRepository).findById(medicoId)
        verify(medicoRepository).save(medico)
    }

    @Test
    fun `deve lancar excecao ao atualizar medico inexistente`() {
        val dadosAtualizacao = DadosAtualizacaoMedico(
            id = 999L,
            nome = "Dr. Inexistente",
            telefone = null,
            endereco = null
        )

        whenever(medicoRepository.findById(dadosAtualizacao.id)).thenReturn(null)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            medicoUseCase.atualizar(dadosAtualizacao)
        }

        assertEquals("Médico com ID 999 não encontrado.", exception.message)
    }

    @Test
    fun `deve excluir medico logicamente`() {
        val medico = TestFixtures.medico(ativo = true)
        val medicoId = medico.id!!

        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)

        medicoUseCase.excluir(medicoId)

        assertFalse(medico.ativo)
        verify(medicoRepository).save(medico)
    }

    @Test
    fun `deve lancar excecao ao excluir medico inativo`() {
        val medico = TestFixtures.medico(ativo = false)
        val medicoId = medico.id!!

        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)

        val exception = assertThrows(IllegalStateException::class.java) {
            medicoUseCase.excluir(medicoId)
        }

        assertEquals("Médico com ID $medicoId já está inativo.", exception.message)
        verify(medicoRepository, never()).save(any())
    }

    @Test
    fun `deve detalhar medico ativo`() {
        val medico = TestFixtures.medico()
        val medicoId = medico.id!!

        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)

        val resultado = medicoUseCase.detalhar(medicoId)

        assertEquals(medico.id, resultado.id)
        assertEquals(medico.nome, resultado.nome)
    }

    @Test
    fun `deve lancar excecao ao detalhar medico inativo`() {
        val medico = TestFixtures.medico(ativo = false)
        val medicoId = medico.id!!

        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            medicoUseCase.detalhar(medicoId)
        }

        assertEquals("Médico com ID $medicoId está inativo.", exception.message)
    }
}
