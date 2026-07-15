package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.TestFixtures
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.*

class MedicoRepositoryAdapterTest {
    private val jpaRepository: MedicoJpaRepository = mock()
    private val adapter = MedicoRepositoryAdapter(jpaRepository)

    @Test
    fun `deve salvar medico`() {
        val medico = TestFixtures.medico()

        whenever(jpaRepository.save(medico)).thenReturn(medico)

        val resultado = adapter.save(medico)

        assertEquals(medico, resultado)
        verify(jpaRepository).save(medico)
    }

    @Test
    fun `deve encontrar medico por id`() {
        val medico = TestFixtures.medico()

        whenever(jpaRepository.findById(1L)).thenReturn(Optional.of(medico))

        val resultado = adapter.findById(1L)

        assertEquals(medico, resultado)
    }

    @Test
    fun `deve retornar null quando medico nao for encontrado`() {
        whenever(jpaRepository.findById(1L)).thenReturn(Optional.empty())

        val resultado = adapter.findById(1L)

        assertNull(resultado)
    }

    @Test
    fun `deve listar medicos ativos paginados`() {
        val paginacao = PageRequest.of(0, 10)
        val medico = TestFixtures.medico()
        val pagina = PageImpl(listOf(medico), paginacao, 1)

        whenever(jpaRepository.findAllByAtivoTrue(paginacao)).thenReturn(pagina)

        val resultado = adapter.findAllByAtivoTrue(paginacao)

        assertEquals(1, resultado.totalElements)
        assertEquals(medico, resultado.content.first())
    }

    @Test
    fun `deve retornar status ativo por id`() {
        whenever(jpaRepository.findAtivoById(1L)).thenReturn(true)

        assertTrue(adapter.findAtivoById(1L) == true)
    }

    @Test
    fun `deve retornar status inativo por id`() {
        whenever(jpaRepository.findAtivoById(1L)).thenReturn(false)

        assertFalse(adapter.findAtivoById(1L) == true)
    }

    @Test
    fun `deve retornar null quando status nao for encontrado`() {
        whenever(jpaRepository.findAtivoById(1L)).thenReturn(null)

        assertNull(adapter.findAtivoById(1L))
    }

    @Test
    fun `deve escolher medico aleatorio livre na data`() {
        val medico = TestFixtures.medico()
        val data = LocalDateTime.now().plusDays(1)

        whenever(
            jpaRepository.escolherMedicoAleatorioLivreNaData(medico.especialidade, data),
        ).thenReturn(medico)

        val resultado = adapter.escolherMedicoAleatorioLivreNaData(medico.especialidade, data)

        assertEquals(medico, resultado)
    }

    @Test
    fun `deve retornar null quando nao houver medico disponivel`() {
        val data = LocalDateTime.now().plusDays(1)

        whenever(
            jpaRepository.escolherMedicoAleatorioLivreNaData(TestFixtures.medico().especialidade, data),
        ).thenReturn(null)

        val resultado = adapter.escolherMedicoAleatorioLivreNaData(TestFixtures.medico().especialidade, data)

        assertNull(resultado)
    }

    @Test
    fun `deve deletar medico por id`() {
        adapter.delete(1L)

        verify(jpaRepository).deleteById(1L)
    }
}
