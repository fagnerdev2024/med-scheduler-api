package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.TestFixtures
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

class PacienteRepositoryAdapterTest {
    private val jpaRepository: PacienteJpaRepository = mock()
    private val adapter = PacienteRepositoryAdapter(jpaRepository)

    @Test
    fun `deve salvar paciente`() {
        val paciente = TestFixtures.paciente()

        whenever(jpaRepository.save(paciente)).thenReturn(paciente)

        val resultado = adapter.save(paciente)

        assertEquals(paciente, resultado)
        verify(jpaRepository).save(paciente)
    }

    @Test
    fun `deve encontrar paciente por id`() {
        val paciente = TestFixtures.paciente()

        whenever(jpaRepository.findById(1L)).thenReturn(Optional.of(paciente))

        val resultado = adapter.findById(1L)

        assertEquals(paciente, resultado)
    }

    @Test
    fun `deve retornar null quando paciente nao for encontrado`() {
        whenever(jpaRepository.findById(1L)).thenReturn(Optional.empty())

        val resultado = adapter.findById(1L)

        assertNull(resultado)
    }

    @Test
    fun `deve listar pacientes ativos paginados`() {
        val paginacao = PageRequest.of(0, 10)
        val paciente = TestFixtures.paciente()
        val pagina = PageImpl(listOf(paciente), paginacao, 1)

        whenever(jpaRepository.findAllByAtivoTrue(paginacao)).thenReturn(pagina)

        val resultado = adapter.findAllByAtivoTrue(paginacao)

        assertEquals(1, resultado.totalElements)
        assertEquals(paciente, resultado.content.first())
    }

    @Test
    fun `deve verificar existencia por email`() {
        whenever(jpaRepository.existsByEmail("paciente@teste.com")).thenReturn(true)

        assertTrue(adapter.existsByEmail("paciente@teste.com"))
    }

    @Test
    fun `deve retornar false quando email nao existir`() {
        whenever(jpaRepository.existsByEmail("outro@teste.com")).thenReturn(false)

        assertFalse(adapter.existsByEmail("outro@teste.com"))
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
    fun `deve deletar paciente por id`() {
        adapter.delete(1L)

        verify(jpaRepository).deleteById(1L)
    }
}
