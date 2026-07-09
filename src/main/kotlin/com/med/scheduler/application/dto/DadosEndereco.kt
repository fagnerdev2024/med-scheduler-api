package com.med.scheduler.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class DadosEndereco(
    @field:NotBlank
    val logradouro: String,

    @field:NotBlank
    val bairro: String,

    @field:NotBlank
    @field:Pattern(regexp = "\\d{8}")
    val cep: String,

    @field:NotBlank
    val cidade: String,

    @field:NotBlank
    val uf: String,

    val complemento: String?,

    val numero: String
)