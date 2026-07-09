package com.med.scheduler.infrastructure.api.controller

import com.med.scheduler.application.dto.DadosAtualizacaoMedico
import com.med.scheduler.application.dto.DadosCadastroMedico
import com.med.scheduler.application.dto.DadosDetalhamentoMedico
import com.med.scheduler.application.dto.DadosListagemMedico
import com.med.scheduler.application.service.MedicoUseCase
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
class MedicoController(
    private val medicoUseCase: MedicoUseCase
) {
    private val log = LoggerFactory.getLogger(MedicoController::class.java)

    @PostMapping
    fun cadastrar(
        @RequestBody @Valid dados: DadosCadastroMedico,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<DadosDetalhamentoMedico> {
        log.info("Recebida solicitação para cadastrar médico: {}", dados.nome)
        val detalhamento = medicoUseCase.cadastrar(dados)
        val uri = uriBuilder.path("/medicos/{id}").buildAndExpand(detalhamento.id).toUri()
        return ResponseEntity.created(uri).body(detalhamento)
    }

    @GetMapping
    fun listar(
        @Parameter(hidden = true) @ParameterObject
        @PageableDefault(page = 0, size = 10, sort = ["nome"]) paginacao: Pageable
    ): ResponseEntity<Any> {
        log.info("Recebida solicitação para listar médicos com paginação: {}", paginacao)
        val medicos = medicoUseCase.listar(paginacao)

        return if (medicos.isEmpty) {
            ResponseEntity.ok<Any>(mapOf("message" to "Nenhum médico ativo encontrado"))
        } else {
            ResponseEntity.ok<Any>(medicos)
        }
    }

    @PutMapping
    fun atualizar(@RequestBody @Valid dados: DadosAtualizacaoMedico): ResponseEntity<DadosDetalhamentoMedico> {
        log.info("Recebida solicitação para atualizar médico com ID: {}", dados.id)
        return ResponseEntity.ok(medicoUseCase.atualizar(dados))
    }

    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Long): ResponseEntity<Void> {
        log.info("Recebida solicitação para excluir médico com ID: {}", id)
        medicoUseCase.excluir(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun detalhar(@PathVariable id: Long): ResponseEntity<DadosDetalhamentoMedico> {
        log.info("Recebida solicitação para detalhar médico com ID: {}", id)
        return ResponseEntity.ok(medicoUseCase.detalhar(id))
    }
}
