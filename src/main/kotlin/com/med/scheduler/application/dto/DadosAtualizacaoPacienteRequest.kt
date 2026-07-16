package com.med.scheduler.application.dto

data class DadosAtualizacaoPacienteRequest(
    val nome: String?,
    val telefone: String?,
    val endereco: DadosEndereco?,
)
