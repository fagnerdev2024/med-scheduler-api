package com.med.scheduler.domain.repository

import com.med.scheduler.domain.model.Consulta
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface ConsultaRepository {
    fun save(consulta: Consulta): Consulta
    fun findById(id: Long): Consulta?
    fun findAll(paginacao: Pageable): Page<Consulta>
    fun delete(id: Long)

    fun existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
        idPaciente: Long,
        primeiroHorario: LocalDateTime,
        ultimoHorario: LocalDateTime
    ): Boolean

    fun existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(
        idMedico: Long,
        data: LocalDateTime
    ): Boolean
}
