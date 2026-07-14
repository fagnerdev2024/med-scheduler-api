package com.med.scheduler.domain.model.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MotivoCancelamentoTest {
    @Test
    fun `deve conter todos os valores`() {
        assertEquals(3, MotivoCancelamento.entries.size)
        assertTrue(MotivoCancelamento.entries.contains(MotivoCancelamento.PACIENTE_DESISTIU))
        assertTrue(MotivoCancelamento.entries.contains(MotivoCancelamento.MEDICO_CANCELOU))
        assertTrue(MotivoCancelamento.entries.contains(MotivoCancelamento.OUTROS))
    }

    @Test
    fun `toString deve conter nome e descricao`() {
        val motivo = MotivoCancelamento.OUTROS

        assertTrue(motivo.toString().contains("OUTROS"))
        assertTrue(motivo.toString().contains("Outros motivos"))
    }

    @Test
    fun `valueOf e values devem funcionar`() {
        assertEquals(MotivoCancelamento.OUTROS, MotivoCancelamento.valueOf("OUTROS"))
        assertEquals(3, MotivoCancelamento.values().size)
    }
}
