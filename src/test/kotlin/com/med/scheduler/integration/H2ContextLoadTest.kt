package com.med.scheduler.integration

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class H2ContextLoadTest {
    @Test
    fun `deve carregar o contexto da aplicacao`() {
    }
}
