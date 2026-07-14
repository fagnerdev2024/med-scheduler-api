package com.med.scheduler.infrastructure.security

import com.med.scheduler.domain.model.Usuario
import com.med.scheduler.domain.repository.UsuarioRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.security.core.userdetails.UsernameNotFoundException

class AutenticacaoServiceTest {
    private val usuarioRepository: UsuarioRepository = mock()
    private val autenticacaoService = AutenticacaoService(usuarioRepository)

    @Test
    fun `deve retornar usuario quando encontrado`() {
        val usuario = Usuario(login = "usuario@teste.com", senha = "senha123")

        whenever(usuarioRepository.findByLogin("usuario@teste.com")).thenReturn(usuario)

        val resultado = autenticacaoService.loadUserByUsername("usuario@teste.com")

        assertEquals(usuario, resultado)
        assertEquals("usuario@teste.com", resultado.username)
    }

    @Test
    fun `deve lancar excecao quando usuario nao for encontrado`() {
        whenever(usuarioRepository.findByLogin("inexistente@teste.com")).thenReturn(null)

        val exception =
            assertThrows(UsernameNotFoundException::class.java) {
                autenticacaoService.loadUserByUsername("inexistente@teste.com")
            }

        assertEquals("Usuário não encontrado: inexistente@teste.com", exception.message)
    }
}
