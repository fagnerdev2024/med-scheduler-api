package com.med.scheduler.application.service

import com.med.scheduler.application.dto.*
import com.med.scheduler.domain.model.Consulta
import com.med.scheduler.domain.model.Medico
import com.med.scheduler.domain.model.Paciente
import com.med.scheduler.domain.repository.ConsultaRepository
import com.med.scheduler.domain.repository.MedicoRepository
import com.med.scheduler.domain.repository.PacienteRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ConsultaUseCase(
    private val consultaRepository: ConsultaRepository,
    private val medicoRepository: MedicoRepository,
    private val pacienteRepository: PacienteRepository
) {
    private val log = LoggerFactory.getLogger(ConsultaUseCase::class.java)

    @Transactional
    fun agendar(dadosAgendamento: DadosAgendamentoConsulta): DadosDetalhamentoConsulta {
        log.info("Iniciando agendamento de consulta para paciente: {}", dadosAgendamento.idPaciente)
        
        val paciente = pacienteRepository.findById(dadosAgendamento.idPaciente)
            ?: throw IllegalArgumentException("Paciente não encontrado.")
        
        if (!paciente.ativo) {
            throw IllegalStateException("Paciente está inativo.")
        }
        
        var medico: Medico? = null
        
        if (dadosAgendamento.idMedico != null) {
            medico = medicoRepository.findById(dadosAgendamento.idMedico)
                ?: throw IllegalArgumentException("Médico não encontrado.")
            
            if (!medico.ativo) {
                throw IllegalStateException("Médico está inativo.")
            }
            
            // Verificar se médico já tem consulta no mesmo horário
            if (consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(
                    medico.id!!, dadosAgendamento.data)) {
                throw IllegalStateException("Médico já possui consulta agendada neste horário.")
            }
        } else {
            // Escolher médico aleatório disponível
            medico = medicoRepository.escolherMedicoAleatorioLivreNaData(
                dadosAgendamento.especialidade!!, dadosAgendamento.data
            ) ?: throw IllegalStateException("Nenhum médico disponível para a especialidade e data informados.")
        }
        
        // Verificar se paciente já tem consulta no mesmo dia
        val inicioDia = dadosAgendamento.data.toLocalDate().atStartOfDay()
        val fimDia = dadosAgendamento.data.toLocalDate().atTime(23, 59, 59)
        
        if (consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
                paciente.id!!, inicioDia, fimDia)) {
            throw IllegalStateException("Paciente já possui consulta agendada neste dia.")
        }
        
        val consulta = Consulta(
            medico = medico,
            paciente = paciente,
            data = dadosAgendamento.data,
            motivoCancelamento = null
        )
        
        val consultaSalva = consultaRepository.save(consulta)
        log.info("Consulta agendada com sucesso: {}", consultaSalva.id)
        
        return consultaSalva.toDetalhamentoDTO()
    }

    @Transactional(readOnly = true)
    fun listar(paginacao: Pageable): Page<DadosDetalhamentoConsulta> {
        log.debug("Listando consultas com paginação: {}", paginacao)
        val consultas = consultaRepository.findAll(paginacao)
        return consultas.map { it.toDetalhamentoDTO() }
    }

    @Transactional
    fun cancelar(dadosCancelamento: DadosCancelamentoConsulta) {
        log.info("Iniciando cancelamento da consulta: {}", dadosCancelamento.idConsulta)
        
        val consulta = consultaRepository.findById(dadosCancelamento.idConsulta)
            ?: throw IllegalArgumentException("Consulta não encontrada.")
        
        if (consulta.motivoCancelamento != null) {
            throw IllegalStateException("Consulta já está cancelada.")
        }
        
        // Verificar antecedência mínima (24 horas)
        val agora = LocalDateTime.now()
        val diferencaHoras = java.time.Duration.between(agora, consulta.data).toHours()
        
        if (diferencaHoras < 24) {
            throw IllegalStateException("Consulta só pode ser cancelada com antecedência mínima de 24 horas.")
        }
        
        consulta.cancelar(dadosCancelamento.motivo)
        consultaRepository.save(consulta)
        log.info("Consulta cancelada com sucesso: {}", consulta.id)
    }

    @Transactional(readOnly = true)
    fun detalhar(id: Long): DadosDetalhamentoConsulta {
        log.info("Iniciando detalhamento da consulta: {}", id)
        
        val consulta = consultaRepository.findById(id)
            ?: throw IllegalArgumentException("Consulta não encontrada.")
        
        return consulta.toDetalhamentoDTO()
    }
}

// Extension functions for DTO conversion
private fun Consulta.toDetalhamentoDTO() = DadosDetalhamentoConsulta(
    id = this.id,
    idMedico = this.medico?.id,
    idPaciente = this.paciente?.id,
    data = this.data,
    motivoCancelamento = this.motivoCancelamento
)