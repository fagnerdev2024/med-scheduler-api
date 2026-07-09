package com.med.scheduler.application.dto

import com.med.scheduler.domain.model.enums.Especialidade
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class DadosCadastroMedico(
    @field:NotBlank
    val nome: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val telefone: String,

    @field:NotBlank
    @field:Pattern(regexp = "\\d{4,6}")
    val crm: String,

    @field:NotNull
    val especialidade: Especialidade,

    @field:NotNull
    @field:Valid
    val endereco: DadosEndereco
)