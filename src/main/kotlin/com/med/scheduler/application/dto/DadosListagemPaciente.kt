package com.med.scheduler.application.dto

data class DadosListagemPaciente(
    val id: Long?,
    val nome: String,
    val email: String,
    val cpf: String,
    val ativo: Boolean,
)
