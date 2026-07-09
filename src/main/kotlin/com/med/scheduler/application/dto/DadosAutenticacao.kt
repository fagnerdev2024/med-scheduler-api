package com.med.scheduler.application.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class DadosAutenticacao(
    @field:NotBlank
    @field:Email
    val login: String,

    @field:NotBlank
    val senha: String
)