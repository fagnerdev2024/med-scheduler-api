package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Usuario
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class UsuarioRepositoryAdapterTest {
    private val jpaRepository: UsuarioJpaRepository = mock()
    private val adapter = UsuarioRepositoryAdapter(jpaRepository)

    @Test
    fun `deve salvar usuario`() {
        val usuario = Usuario(login = "usuario@teste.com", senha = "senha123")

        whenever(jpaRepository.save(usuario)).thenReturn(usuario)

        val resultado = adapter.save(usuario)

        assertEquals(usuario, resultado)
        verify(jpaRepository).save(usuario)
    }

    @Test
    fun `deve encontrar usuario por login`() {
        val usuario = Usuario(login = "usuario@teste.com", senha = "senha123")

        whenever(jpaRepository.findByLogin("usuario@teste.com")).thenReturn(usuario)

        val resultado = adapter.findByLogin("usuario@teste.com")

        assertEquals(usuario, resultado)
    }

    @Test
    fun `deve retornar null quando usuario nao for encontrado`() {
        whenever(jpaRepository.findByLogin("inexistente@teste.com")).thenReturn(null)

        val resultado = adapter.findByLogin("inexistente@teste.com")

        assertNull(resultado)
    }
}
