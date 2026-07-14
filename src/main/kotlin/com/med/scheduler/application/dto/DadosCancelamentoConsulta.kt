package com.med.scheduler.application.dto

import com.med.scheduler.domain.model.enums.MotivoCancelamento
import jakarta.validation.constraints.NotNull

data class DadosCancelamentoConsulta(
    @field:NotNull
    val idConsulta: Long,
    @field:NotNull
    val motivo: MotivoCancelamento,
)
