package com.med.scheduler.application.dto

import com.med.scheduler.domain.model.enums.MotivoCancelamento
import java.time.LocalDateTime

data class DadosDetalhamentoConsulta(
    val id: Long?,
    val idMedico: Long?,
    val idPaciente: Long?,
    val data: LocalDateTime,
    val motivoCancelamento: MotivoCancelamento?,
)
