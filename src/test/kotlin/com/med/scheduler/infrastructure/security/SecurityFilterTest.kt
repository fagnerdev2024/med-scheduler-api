package com.med.scheduler.infrastructure.security

import com.med.scheduler.domain.model.Usuario
import com.med.scheduler.domain.repository.UsuarioRepository
import com.med.scheduler.domain.service.TokenService
import jakarta.servlet.FilterChain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder

class SecurityFilterTest {
    private val tokenService: TokenService = mock()
    private val usuarioRepository: UsuarioRepository = mock()
    private val securityFilter = SecurityFilter(tokenService, usuarioRepository)

    @BeforeEach
    @AfterEach
    fun limparContexto() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `deve continuar chain quando nao houver token`() {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val chain: FilterChain = mock()

        securityFilter.doFilter(request, response, chain)

        verify(chain).doFilter(request, response)
        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `deve autenticar quando token for valido`() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer token-valido")
        val response = MockHttpServletResponse()
        val chain: FilterChain = mock()
        val usuario = Usuario(login = "usuario@teste.com", senha = "senha123")

        whenever(tokenService.getSubject("token-valido")).thenReturn("usuario@teste.com")
        whenever(usuarioRepository.findByLogin("usuario@teste.com")).thenReturn(usuario)

        securityFilter.doFilter(request, response, chain)

        verify(chain).doFilter(request, response)
        assertNotNull(SecurityContextHolder.getContext().authentication)
        assertEquals(usuario, SecurityContextHolder.getContext().authentication.principal)
    }

    @Test
    fun `deve continuar quando subject estiver em branco`() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer token-em-branco")
        val response = MockHttpServletResponse()
        val chain: FilterChain = mock()

        whenever(tokenService.getSubject("token-em-branco")).thenReturn("   ")

        securityFilter.doFilter(request, response, chain)

        verify(chain).doFilter(request, response)
        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `deve continuar quando usuario nao for encontrado`() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer token-valido")
        val response = MockHttpServletResponse()
        val chain: FilterChain = mock()

        whenever(tokenService.getSubject("token-valido")).thenReturn("usuario@teste.com")
        whenever(usuarioRepository.findByLogin("usuario@teste.com")).thenReturn(null)

        securityFilter.doFilter(request, response, chain)

        verify(chain).doFilter(request, response)
        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `deve continuar quando token for invalido`() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer token-invalido")
        val response = MockHttpServletResponse()
        val chain: FilterChain = mock()

        whenever(tokenService.getSubject("token-invalido")).thenReturn(null)

        securityFilter.doFilter(request, response, chain)

        verify(chain).doFilter(request, response)
        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `deve ignorar header sem prefixo Bearer`() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Basic token")
        val response = MockHttpServletResponse()
        val chain: FilterChain = mock()

        securityFilter.doFilter(request, response, chain)

        verify(chain).doFilter(request, response)
        verifyNoInteractions(tokenService, usuarioRepository)
        assertNull(SecurityContextHolder.getContext().authentication)
    }
}
