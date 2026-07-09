package com.med.scheduler.application.dto

import jakarta.validation.constraints.NotBlank

data class DadosAtualizacaoMedico(
    val id: Long,
    val nome: String?,
    val telefone: String?,
    val endereco: DadosEndereco?
)