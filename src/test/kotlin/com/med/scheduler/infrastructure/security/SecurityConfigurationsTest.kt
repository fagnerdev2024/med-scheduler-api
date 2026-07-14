package com.med.scheduler.infrastructure.security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class SecurityConfigurationsTest {
    private val securityFilter: SecurityFilter = mock()
    private val configurations = SecurityConfigurations(securityFilter)

    @Test
    fun `deve configurar cors`() {
        val source = configurations.corsConfigurationSource()
        val request = MockHttpServletRequest()
        request.setMethod("GET")

        val config = source.getCorsConfiguration(request)

        assertNotNull(config)
        assertTrue(config!!.allowedMethods!!.containsAll(listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")))
        assertTrue(config.allowedHeaders!!.contains("*"))
        assertTrue(config.allowCredentials == true)
    }

    @Test
    fun `deve criar password encoder com BCrypt`() {
        val passwordEncoder = configurations.passwordEncoder()

        assertTrue(passwordEncoder is BCryptPasswordEncoder)
        assertNotNull(passwordEncoder.encode("senha"))
    }

    @Test
    fun `deve retornar authentication manager`() {
        val authConfig = mock<AuthenticationConfiguration>()
        val manager = mock<AuthenticationManager>()

        whenever(authConfig.getAuthenticationManager()).thenReturn(manager)

        val resultado = configurations.authenticationManager(authConfig)

        assertEquals(manager, resultado)
    }
}
