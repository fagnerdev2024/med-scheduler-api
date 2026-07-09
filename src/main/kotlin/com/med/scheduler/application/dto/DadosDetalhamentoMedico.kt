package com.med.scheduler.application.dto

import com.med.scheduler.domain.model.enums.Especialidade

data class DadosDetalhamentoMedico(
    val id: Long?,
    val nome: String,
    val email: String,
    val crm: String,
    val telefone: String,
    val especialidade: Especialidade,
    val endereco: DadosEndereco,
    val ativo: Boolean
)