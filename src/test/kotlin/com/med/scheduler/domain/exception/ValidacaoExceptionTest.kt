package com.med.scheduler.domain.exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ValidacaoExceptionTest {
    @Test
    fun `deve criar excecao com mensagem`() {
        val exception = ValidacaoException("mensagem de erro")

        assertEquals("mensagem de erro", exception.message)
    }
}
