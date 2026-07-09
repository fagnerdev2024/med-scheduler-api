package com.med.scheduler.domain.repository

import com.med.scheduler.domain.model.Consulta
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ConsultaRepository : JpaRepository<Consulta, Long> {

    fun existsByPacienteIdAndDataBetween(
        idPaciente: Long,
        primeiroHorario: LocalDateTime,
        ultimoHorario: LocalDateTime
    ): Boolean

    fun existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(
        idMedico: Long,
        data: LocalDateTime
    ): Boolean
}
