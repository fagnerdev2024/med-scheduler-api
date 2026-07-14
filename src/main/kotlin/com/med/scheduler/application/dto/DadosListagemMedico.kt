package com.med.scheduler.application.dto

import com.med.scheduler.domain.model.enums.Especialidade

data class DadosListagemMedico(
    val id: Long?,
    val nome: String,
    val email: String,
    val crm: String,
    val especialidade: Especialidade,
    val ativo: Boolean,
)
