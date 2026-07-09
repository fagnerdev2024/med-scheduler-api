package com.med.scheduler.application.dto

import com.med.scheduler.domain.model.enums.Especialidade
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class DadosAgendamentoConsulta(
    @field:NotNull
    val idMedico: Long?,

    @field:NotNull
    val idPaciente: Long,

    @field:NotNull
    @field:Future
    val data: LocalDateTime,

    val especialidade: Especialidade?
)