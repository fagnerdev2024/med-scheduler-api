package com.med.scheduler.infrastructure.api.controller

import com.med.scheduler.application.dto.DadosAgendamentoConsulta
import com.med.scheduler.application.dto.DadosCancelamentoConsulta
import com.med.scheduler.application.dto.DadosDetalhamentoConsulta
import com.med.scheduler.application.service.ConsultaUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/consultas")
@SecurityRequirement(name = "bearer-key")
class ConsultaController(
    private val consultaUseCase: ConsultaUseCase,
) {
    @PostMapping
    @Operation(summary = "Agenda uma nova consulta")
    fun agendar(
        @RequestBody @Valid dados: DadosAgendamentoConsulta,
    ): ResponseEntity<DadosDetalhamentoConsulta> {
        return ResponseEntity.ok(consultaUseCase.agendar(dados))
    }

    @DeleteMapping
    @Operation(summary = "Cancela uma consulta existente")
    fun cancelar(
        @RequestBody @Valid dados: DadosCancelamentoConsulta,
    ): ResponseEntity<Void> {
        consultaUseCase.cancelar(dados)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    @Operation(summary = "Lista todas as consultas agendadas")
    fun listar(
        @Parameter(hidden = true) @ParameterObject
        @PageableDefault(page = 0, size = 10, sort = ["data"]) paginacao: Pageable,
    ): ResponseEntity<Page<DadosDetalhamentoConsulta>> {
        return ResponseEntity.ok(consultaUseCase.listar(paginacao))
    }
}
