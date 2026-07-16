package com.med.scheduler.application.dto

data class DadosAtualizacaoMedicoRequest(
    val nome: String?,
    val telefone: String?,
    val endereco: DadosEndereco?,
)
