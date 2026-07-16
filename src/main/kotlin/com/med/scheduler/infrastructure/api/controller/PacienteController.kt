package com.med.scheduler.infrastructure.api.controller

import com.med.scheduler.application.dto.DadosAtualizacaoPacienteRequest
import com.med.scheduler.application.dto.DadosCadastroPaciente
import com.med.scheduler.application.dto.DadosDetalhamentoPaciente
import com.med.scheduler.application.dto.toUseCase
import com.med.scheduler.application.service.PacienteUseCase
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/pacientes")
@SecurityRequirement(name = "bearer-key")
class PacienteController(
    private val pacienteUseCase: PacienteUseCase,
) {
    private val log = LoggerFactory.getLogger(PacienteController::class.java)

    @PostMapping
    fun cadastrar(
        @RequestBody @Valid dados: DadosCadastroPaciente,
        uriBuilder: UriComponentsBuilder,
    ): ResponseEntity<DadosDetalhamentoPaciente> {
        log.info("Recebida solicitação para cadastrar paciente: {}", dados.nome)
        val detalhamento = pacienteUseCase.cadastrar(dados)
        val uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(detalhamento.id).toUri()
        return ResponseEntity.created(uri).body(detalhamento)
    }

    @GetMapping
    fun listar(
        @Parameter(hidden = true) @ParameterObject
        @PageableDefault(page = 0, size = 10, sort = ["nome"]) paginacao: Pageable,
    ): ResponseEntity<Any> {
        log.info("Recebida solicitação para listar pacientes com paginação: {}", paginacao)
        val pacientes = pacienteUseCase.listar(paginacao)

        return if (pacientes.isEmpty) {
            ResponseEntity.ok<Any>(mapOf("message" to "Nenhum paciente ativo encontrado"))
        } else {
            ResponseEntity.ok<Any>(pacientes)
        }
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Long,
        @RequestBody @Valid dados: DadosAtualizacaoPacienteRequest,
    ): ResponseEntity<DadosDetalhamentoPaciente> {
        log.info("Recebida solicitação para atualizar paciente com ID: {}", id)
        return ResponseEntity.ok(pacienteUseCase.atualizar(dados.toUseCase(id)))
    }

    @DeleteMapping("/{id}")
    fun excluir(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        log.info("Recebida solicitação para excluir paciente com ID: {}", id)
        pacienteUseCase.excluir(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun detalhar(
        @PathVariable id: Long,
    ): ResponseEntity<DadosDetalhamentoPaciente> {
        log.info("Recebida solicitação para detalhar paciente com ID: {}", id)
        return ResponseEntity.ok(pacienteUseCase.detalhar(id))
    }
}
