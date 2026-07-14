package com.med.scheduler.application.service

import com.med.scheduler.application.dto.*
import com.med.scheduler.domain.model.Consulta
import com.med.scheduler.domain.repository.ConsultaRepository
import com.med.scheduler.domain.repository.MedicoRepository
import com.med.scheduler.domain.repository.PacienteRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime

@Service
class ConsultaUseCase(
    private val consultaRepository: ConsultaRepository,
    private val medicoRepository: MedicoRepository,
    private val pacienteRepository: PacienteRepository,
) {
    private val log = LoggerFactory.getLogger(ConsultaUseCase::class.java)

    @Transactional
    fun agendar(dadosAgendamento: DadosAgendamentoConsulta): DadosDetalhamentoConsulta {
        log.info("Iniciando agendamento de consulta para paciente: {}", dadosAgendamento.idPaciente)

        validarHorarioAgendamento(dadosAgendamento.data)

        val paciente =
            pacienteRepository.findById(dadosAgendamento.idPaciente)
                ?: throw IllegalArgumentException("Paciente não encontrado.")

        if (!paciente.ativo) {
            throw IllegalStateException("Paciente está inativo.")
        }

        val medico =
            if (dadosAgendamento.idMedico != null) {
                val medicoEscolhido =
                    medicoRepository.findById(dadosAgendamento.idMedico)
                        ?: throw IllegalArgumentException("Médico não encontrado.")

                if (!medicoEscolhido.ativo) {
                    throw IllegalStateException("Médico está inativo.")
                }

                if (consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(
                        medicoEscolhido.id!!,
                        dadosAgendamento.data,
                    )
                ) {
                    throw IllegalStateException("Médico já possui consulta agendada neste horário.")
                }

                medicoEscolhido
            } else {
                val especialidade =
                    dadosAgendamento.especialidade
                        ?: throw IllegalArgumentException("Especialidade é obrigatória quando médico não for escolhido.")

                medicoRepository.escolherMedicoAleatorioLivreNaData(especialidade, dadosAgendamento.data)
                    ?: throw IllegalStateException("Nenhum médico disponível para a especialidade e data informados.")
            }

        val inicioDia = dadosAgendamento.data.withHour(7)
        val fimDia = dadosAgendamento.data.withHour(18)

        if (consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
                paciente.id!!,
                inicioDia,
                fimDia,
            )
        ) {
            throw IllegalStateException("Paciente já possui consulta agendada neste dia.")
        }

        val consulta =
            Consulta(
                medico = medico,
                paciente = paciente,
                data = dadosAgendamento.data,
                motivoCancelamento = null,
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

        val consulta =
            consultaRepository.findById(dadosCancelamento.idConsulta)
                ?: throw IllegalArgumentException("Consulta não encontrada.")

        if (consulta.motivoCancelamento != null) {
            throw IllegalStateException("Consulta já está cancelada.")
        }

        val agora = LocalDateTime.now()
        val diferencaHoras = Duration.between(agora, consulta.data).toHours()

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

        val consulta =
            consultaRepository.findById(id)
                ?: throw IllegalArgumentException("Consulta não encontrada.")

        return consulta.toDetalhamentoDTO()
    }

    private fun validarHorarioAgendamento(data: LocalDateTime) {
        val agora = LocalDateTime.now()
        val diferencaMinutos = Duration.between(agora, data).toMinutes()

        if (diferencaMinutos < 30) {
            throw IllegalStateException("Consulta deve ser agendada com pelo menos 30 minutos de antecedência.")
        }

        if (data.dayOfWeek == DayOfWeek.SUNDAY || data.hour < 7 || data.hour > 18) {
            throw IllegalStateException("Consulta fora do horário de funcionamento da clínica (Seg-Sáb, 07:00 às 18:00).")
        }
    }
}
