package com.med.scheduler.application.dto

data class DadosDetalhamentoPaciente(
    val id: Long?,
    val nome: String,
    val email: String,
    val cpf: String,
    val telefone: String,
    val endereco: DadosEndereco,
    val ativo: Boolean,
)
