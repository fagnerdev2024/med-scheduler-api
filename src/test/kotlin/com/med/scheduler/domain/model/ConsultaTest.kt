package com.med.scheduler.domain.model

import com.med.scheduler.TestFixtures
import com.med.scheduler.domain.model.enums.MotivoCancelamento
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ConsultaTest {
    @Test
    fun `deve cancelar consulta alterando motivo`() {
        val consulta = TestFixtures.consulta()

        consulta.cancelar(MotivoCancelamento.OUTROS)

        assertEquals(MotivoCancelamento.OUTROS, consulta.motivoCancelamento)
    }

    @Test
    fun `deve permitir alteracao das propriedades`() {
        val consulta = TestFixtures.consulta()
        val novoMedico = TestFixtures.medico(id = 2L, nome = "Novo")
        val novoPaciente = TestFixtures.paciente(id = 2L, nome = "Novo")
        val novaData = LocalDateTime.now().plusDays(2)

        consulta.medico = novoMedico
        consulta.paciente = novoPaciente
        consulta.data = novaData

        assertEquals(novoMedico, consulta.medico)
        assertEquals(novoPaciente, consulta.paciente)
        assertEquals(novaData, consulta.data)
    }

    @Test
    fun `deve ter motivoCancelamento nulo por padrao`() {
        val consulta =
            Consulta(
                id = null,
                medico = TestFixtures.medico(id = 1L),
                paciente = TestFixtures.paciente(id = 1L),
                data = LocalDateTime.now().plusDays(1),
            )

        assertNull(consulta.motivoCancelamento)
    }

    @Test
    fun `deve ser igual quando ids forem iguais`() {
        val consulta1 = TestFixtures.consulta(id = 1L)
        val consulta2 = TestFixtures.consulta(id = 1L)

        assertEquals(consulta1, consulta2)
        assertEquals(consulta1.hashCode(), consulta2.hashCode())
    }

    @Test
    fun `nao deve ser igual quando ids forem diferentes`() {
        val consulta1 = TestFixtures.consulta(id = 1L)
        val consulta2 = TestFixtures.consulta(id = 2L)

        assertNotEquals(consulta1, consulta2)
    }

    @Test
    fun `nao deve ser igual quando id for nulo`() {
        val consulta1 = TestFixtures.consulta(id = null)
        val consulta2 = TestFixtures.consulta(id = null)

        assertNotEquals(consulta1, consulta2)
        assertEquals(0, consulta1.hashCode())
    }

    @Test
    fun `nao deve ser igual a outro tipo`() {
        val consulta = TestFixtures.consulta()

        assertNotEquals(consulta, "consulta")
    }
}
