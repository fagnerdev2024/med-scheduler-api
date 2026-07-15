package com.med.scheduler.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UsuarioTest {
    @Test
    fun `deve retornar authorities`() {
        val usuario = Usuario(login = "usuario@teste.com", senha = "senha123")

        val authorities = usuario.authorities

        assertEquals(1, authorities.size)
        assertTrue(authorities.contains(SimpleGrantedAuthority("ROLE_USER")))
    }

    @Test
    fun `deve retornar senha e login`() {
        val usuario = Usuario(login = "usuario@teste.com", senha = "senha123")

        assertEquals("senha123", usuario.password)
        assertEquals("usuario@teste.com", usuario.username)
    }

    @Test
    fun `deve retornar flags de conta ativas`() {
        val usuario = Usuario(login = "usuario@teste.com", senha = "senha123")

        assertTrue(usuario.isAccountNonExpired)
        assertTrue(usuario.isAccountNonLocked)
        assertTrue(usuario.isCredentialsNonExpired)
        assertTrue(usuario.isEnabled)
    }

    @Test
    fun `deve permitir alteracao de login e senha`() {
        val usuario = Usuario(login = "usuario@teste.com", senha = "senha123")

        usuario.login = "novo@example.com"
        usuario.senha = "nova-senha"

        assertEquals("novo@example.com", usuario.username)
        assertEquals("nova-senha", usuario.password)
    }

    @Test
    fun `deve ser igual quando ids forem iguais`() {
        val usuario1 = Usuario(id = 1L, login = "a@teste.com", senha = "senha1")
        val usuario2 = Usuario(id = 1L, login = "b@teste.com", senha = "senha2")

        assertEquals(usuario1, usuario2)
        assertEquals(usuario1.hashCode(), usuario2.hashCode())
    }

    @Test
    fun `nao deve ser igual quando ids forem diferentes`() {
        val usuario1 = Usuario(id = 1L, login = "a@teste.com", senha = "senha")
        val usuario2 = Usuario(id = 2L, login = "a@teste.com", senha = "senha")

        assertNotEquals(usuario1, usuario2)
    }

    @Test
    fun `nao deve ser igual quando id for nulo`() {
        val usuario1 = Usuario(id = null, login = "a@teste.com", senha = "senha")
        val usuario2 = Usuario(id = null, login = "a@teste.com", senha = "senha")

        assertNotEquals(usuario1, usuario2)
        assertEquals(0, usuario1.hashCode())
    }

    @Test
    fun `nao deve ser igual a outro tipo`() {
        val usuario = Usuario(login = "a@teste.com", senha = "senha")

        assertNotEquals(usuario, "usuario")
    }
}
