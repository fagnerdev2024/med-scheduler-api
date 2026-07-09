package com.med.scheduler.infrastructure.api.controller

import com.med.scheduler.application.dto.DadosAutenticacao
import com.med.scheduler.application.dto.DadosTokenJWT
import com.med.scheduler.application.service.AutenticacaoUseCase
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class AutenticacaoController(
    private val autenticacaoUseCase: AutenticacaoUseCase
) {
    private val log = LoggerFactory.getLogger(AutenticacaoController::class.java)

    @PostMapping
    fun efetuarLogin(@RequestBody @Valid dados: DadosAutenticacao): ResponseEntity<DadosTokenJWT> {
        log.info("Tentativa de autenticação do usuário: {}", dados.login)
        val token = autenticacaoUseCase.autenticar(dados)
        log.info("Autenticação bem-sucedida para o usuário: {}", dados.login)
        return ResponseEntity.ok(token)
    }
}
