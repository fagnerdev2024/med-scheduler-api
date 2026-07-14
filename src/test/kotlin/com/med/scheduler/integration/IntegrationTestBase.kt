package com.med.scheduler.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.med.scheduler.domain.model.Usuario
import com.med.scheduler.domain.repository.UsuarioRepository
import com.med.scheduler.domain.service.TokenService
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
abstract class IntegrationTestBase {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var tokenService: TokenService

    @Autowired
    protected lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    protected lateinit var usuarioRepository: UsuarioRepository

    protected lateinit var token: String

    @BeforeEach
    fun setup() {
        val usuario =
            usuarioRepository.save(
                Usuario(
                    login = "teste@example.com",
                    senha = passwordEncoder.encode("password"),
                ),
            )
        token = tokenService.gerarToken(usuario)
    }
}
