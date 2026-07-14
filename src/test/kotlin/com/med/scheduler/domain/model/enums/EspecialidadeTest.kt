package com.med.scheduler.domain.model.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EspecialidadeTest {
    @Test
    fun `deve conter todos os valores`() {
        assertEquals(6, Especialidade.entries.size)
        assertTrue(Especialidade.entries.contains(Especialidade.ORTOPEDIA))
        assertTrue(Especialidade.entries.contains(Especialidade.CARDIOLOGIA))
        assertTrue(Especialidade.entries.contains(Especialidade.GINECOLOGIA))
        assertTrue(Especialidade.entries.contains(Especialidade.DERMATOLOGIA))
        assertTrue(Especialidade.entries.contains(Especialidade.NEUROLOGIA))
        assertTrue(Especialidade.entries.contains(Especialidade.PEDIATRIA))
    }

    @Test
    fun `toString deve conter nome e descricao`() {
        val especialidade = Especialidade.CARDIOLOGIA

        assertTrue(especialidade.toString().contains("CARDIOLOGIA"))
        assertTrue(especialidade.toString().contains("Diagnóstico"))
    }

    @Test
    fun `valueOf e values devem funcionar`() {
        assertEquals(Especialidade.CARDIOLOGIA, Especialidade.valueOf("CARDIOLOGIA"))
        assertEquals(6, Especialidade.values().size)
    }
}
