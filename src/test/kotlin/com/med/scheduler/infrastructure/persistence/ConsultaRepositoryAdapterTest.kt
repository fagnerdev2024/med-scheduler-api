package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.TestFixtures
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.*

class ConsultaRepositoryAdapterTest {
    private val jpaRepository: ConsultaJpaRepository = mock()
    private val adapter = ConsultaRepositoryAdapter(jpaRepository)

    @Test
    fun `deve salvar consulta`() {
        val consulta = TestFixtures.consulta()

        whenever(jpaRepository.save(consulta)).thenReturn(consulta)

        val resultado = adapter.save(consulta)

        assertEquals(consulta, resultado)
        verify(jpaRepository).save(consulta)
    }

    @Test
    fun `deve encontrar consulta por id`() {
        val consulta = TestFixtures.consulta()

        whenever(jpaRepository.findById(1L)).thenReturn(Optional.of(consulta))

        val resultado = adapter.findById(1L)

        assertEquals(consulta, resultado)
    }

    @Test
    fun `deve retornar null quando consulta nao for encontrada`() {
        whenever(jpaRepository.findById(1L)).thenReturn(Optional.empty())

        val resultado = adapter.findById(1L)

        assertNull(resultado)
    }

    @Test
    fun `deve listar consultas paginadas`() {
        val paginacao = PageRequest.of(0, 10)
        val consulta = TestFixtures.consulta()
        val pagina = PageImpl(listOf(consulta), paginacao, 1)

        whenever(jpaRepository.findAll(paginacao)).thenReturn(pagina)

        val resultado = adapter.findAll(paginacao)

        assertEquals(1, resultado.totalElements)
        assertEquals(consulta, resultado.content.first())
    }

    @Test
    fun `deve verificar existencia de consulta por paciente e intervalo`() {
        val pacienteId = 1L
        val inicio = LocalDateTime.now().withHour(7)
        val fim = LocalDateTime.now().withHour(18)

        whenever(
            jpaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(pacienteId, inicio, fim),
        ).thenReturn(true)

        assertTrue(adapter.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(pacienteId, inicio, fim))
    }

    @Test
    fun `deve verificar existencia de consulta por medico e data`() {
        val medicoId = 1L
        val data = LocalDateTime.now().plusDays(1)

        whenever(
            jpaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medicoId, data),
        ).thenReturn(false)

        assertFalse(adapter.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medicoId, data))
    }

    @Test
    fun `deve deletar consulta por id`() {
        adapter.delete(1L)

        verify(jpaRepository).deleteById(1L)
    }
}
