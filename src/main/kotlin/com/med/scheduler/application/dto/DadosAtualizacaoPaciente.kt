package com.med.scheduler.application.dto

data class DadosAtualizacaoPaciente(
    val id: Long,
    val nome: String?,
    val telefone: String?,
    val endereco: DadosEndereco?,
)
