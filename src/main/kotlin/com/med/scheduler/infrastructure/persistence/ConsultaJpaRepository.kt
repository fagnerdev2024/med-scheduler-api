package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Consulta
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ConsultaJpaRepository : JpaRepository<Consulta, Long> {
    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM Consulta c
        WHERE c.paciente.id = :pacienteId
        AND c.data BETWEEN :inicio AND :fim
        AND c.motivoCancelamento IS NULL
    """)
    fun existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
        pacienteId: Long,
        inicio: LocalDateTime,
        fim: LocalDateTime
    ): Boolean

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM Consulta c
        WHERE c.medico.id = :medicoId
        AND c.data = :data
        AND c.motivoCancelamento IS NULL
    """)
    fun existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(
        medicoId: Long,
        data: LocalDateTime
    ): Boolean
}