package com.med.scheduler.application.service

import com.med.scheduler.TestFixtures
import com.med.scheduler.domain.model.Consulta
import com.med.scheduler.domain.model.enums.Especialidade
import com.med.scheduler.domain.model.enums.MotivoCancelamento
import com.med.scheduler.domain.repository.ConsultaRepository
import com.med.scheduler.domain.repository.MedicoRepository
import com.med.scheduler.domain.repository.PacienteRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.DayOfWeek
import java.time.LocalDateTime

class ConsultaUseCaseTest {
    private val consultaRepository: ConsultaRepository = mock()
    private val medicoRepository: MedicoRepository = mock()
    private val pacienteRepository: PacienteRepository = mock()
    private val consultaUseCase = ConsultaUseCase(consultaRepository, medicoRepository, pacienteRepository)

    @BeforeEach
    fun setUp() {
        clearInvocations(consultaRepository, medicoRepository, pacienteRepository)
    }

    private fun dataFutura(
        hora: Int = 10,
        minuto: Int = 0,
    ): LocalDateTime {
        var data =
            LocalDateTime.now()
                .plusDays(1)
                .withHour(hora)
                .withMinute(minuto)
                .withSecond(0)
                .withNano(0)

        while (data.dayOfWeek == DayOfWeek.SUNDAY) {
            data = data.plusDays(1)
        }

        return data
    }

    @Test
    fun `deve agendar consulta com medico escolhido`() {
        val data = dataFutura()
        val medico = TestFixtures.medico()
        val medicoId = medico.id!!
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idMedico = medicoId,
                idPaciente = pacienteId,
                data = data,
            )
        val consultaEsperada = TestFixtures.consulta(data = data, medico = medico, paciente = paciente)

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)
        whenever(
            consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medicoId, data),
        ).thenReturn(false)
        whenever(
            consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
                pacienteId,
                data.withHour(7),
                data.withHour(18),
            ),
        ).thenReturn(false)
        whenever(consultaRepository.save(any())).thenReturn(consultaEsperada)

        val resultado = consultaUseCase.agendar(dadosAgendamento)

        assertEquals(medicoId, resultado.idMedico)
        assertEquals(pacienteId, resultado.idPaciente)
        assertEquals(data, resultado.data)

        verify(consultaRepository).save(any())
    }

    @Test
    fun `deve agendar consulta com medico aleatorio`() {
        val data = dataFutura()
        val medico = TestFixtures.medico()
        val medicoId = medico.id!!
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val especialidade = Especialidade.CARDIOLOGIA
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idMedico = null,
                idPaciente = pacienteId,
                data = data,
                especialidade = especialidade,
            )
        val consultaEsperada = TestFixtures.consulta(data = data, medico = medico, paciente = paciente)

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(
            medicoRepository.escolherMedicoAleatorioLivreNaData(especialidade, data),
        ).thenReturn(medico)
        whenever(
            consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
                pacienteId,
                data.withHour(7),
                data.withHour(18),
            ),
        ).thenReturn(false)
        whenever(consultaRepository.save(any())).thenReturn(consultaEsperada)

        val resultado = consultaUseCase.agendar(dadosAgendamento)

        assertEquals(medicoId, resultado.idMedico)
        assertEquals(pacienteId, resultado.idPaciente)

        verify(medicoRepository).escolherMedicoAleatorioLivreNaData(especialidade, data)
        verify(consultaRepository).save(any())
    }

    @Test
    fun `deve lancar excecao quando horario de agendamento for inferior a 30 minutos`() {
        val data = LocalDateTime.now().plusMinutes(10)
        val dadosAgendamento = TestFixtures.dadosAgendamentoConsulta(data = data)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals("Consulta deve ser agendada com pelo menos 30 minutos de antecedência.", exception.message)
        verifyNoInteractions(pacienteRepository, medicoRepository, consultaRepository)
    }

    @Test
    fun `deve lancar excecao quando agendamento for em domingo`() {
        var data = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0)
        while (data.dayOfWeek != DayOfWeek.SUNDAY) {
            data = data.plusDays(1)
        }

        val dadosAgendamento = TestFixtures.dadosAgendamentoConsulta(data = data)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals(
            "Consulta fora do horário de funcionamento da clínica (Seg-Sáb, 07:00 às 18:00).",
            exception.message,
        )
        verifyNoInteractions(pacienteRepository, medicoRepository, consultaRepository)
    }

    @Test
    fun `deve lancar excecao quando horario de agendamento for apos as 18h`() {
        val data = dataFutura(hora = 19)
        val dadosAgendamento = TestFixtures.dadosAgendamentoConsulta(data = data)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals(
            "Consulta fora do horário de funcionamento da clínica (Seg-Sáb, 07:00 às 18:00).",
            exception.message,
        )
    }

    @Test
    fun `deve lancar excecao quando paciente estiver inativo`() {
        val data = dataFutura()
        val paciente = TestFixtures.paciente(ativo = false)
        val pacienteId = paciente.id!!
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idPaciente = pacienteId,
                data = data,
            )

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals("Paciente está inativo.", exception.message)
    }

    @Test
    fun `deve lancar excecao quando medico nao for encontrado`() {
        val data = dataFutura()
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idPaciente = pacienteId,
                data = data,
            )

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(medicoRepository.findById(dadosAgendamento.idMedico!!)).thenReturn(null)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals("Médico não encontrado.", exception.message)
    }

    @Test
    fun `deve lancar excecao quando medico estiver inativo`() {
        val data = dataFutura()
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val medico = TestFixtures.medico(ativo = false)
        val medicoId = medico.id!!
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idMedico = medicoId,
                idPaciente = pacienteId,
                data = data,
            )

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals("Médico está inativo.", exception.message)
    }

    @Test
    fun `deve lancar excecao quando medico ja possui consulta no horario`() {
        val data = dataFutura()
        val medico = TestFixtures.medico()
        val medicoId = medico.id!!
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idMedico = medicoId,
                idPaciente = pacienteId,
                data = data,
            )

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)
        whenever(
            consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medicoId, data),
        ).thenReturn(true)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals("Médico já possui consulta agendada neste horário.", exception.message)
    }

    @Test
    fun `deve lancar excecao quando paciente ja possui consulta no dia`() {
        val data = dataFutura()
        val medico = TestFixtures.medico()
        val medicoId = medico.id!!
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idMedico = medicoId,
                idPaciente = pacienteId,
                data = data,
            )

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)
        whenever(
            consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medicoId, data),
        ).thenReturn(false)
        whenever(
            consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
                pacienteId,
                data.withHour(7),
                data.withHour(18),
            ),
        ).thenReturn(true)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals("Paciente já possui consulta agendada neste dia.", exception.message)
    }

    @Test
    fun `deve listar consultas com paginacao`() {
        val paginacao = PageRequest.of(0, 10)
        val consulta = TestFixtures.consulta()
        val pagina = PageImpl(listOf(consulta), paginacao, 1)

        whenever(consultaRepository.findAll(paginacao)).thenReturn(pagina)

        val resultado = consultaUseCase.listar(paginacao)

        assertEquals(1, resultado.totalElements)
        assertEquals(consulta.id, resultado.content.first().id)

        verify(consultaRepository).findAll(paginacao)
    }

    @Test
    fun `deve detalhar consulta`() {
        val consulta = TestFixtures.consulta()
        val consultaId = consulta.id!!

        whenever(consultaRepository.findById(consultaId)).thenReturn(consulta)

        val resultado = consultaUseCase.detalhar(consultaId)

        assertEquals(consulta.id, resultado.id)
        assertEquals(consulta.data, resultado.data)
    }

    @Test
    fun `deve cancelar consulta com mais de 24 horas de antecedencia`() {
        val data = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0)
        val consulta = TestFixtures.consulta(data = data)
        val consultaId = consulta.id!!
        val dadosCancelamento = TestFixtures.dadosCancelamentoConsulta(idConsulta = consultaId)

        whenever(consultaRepository.findById(consultaId)).thenReturn(consulta)

        consultaUseCase.cancelar(dadosCancelamento)

        assertEquals(MotivoCancelamento.PACIENTE_DESISTIU, consulta.motivoCancelamento)
        verify(consultaRepository).save(consulta)
    }

    @Test
    fun `deve lancar excecao ao cancelar consulta ja cancelada`() {
        val data = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0)
        val consulta =
            TestFixtures.consulta(
                data = data,
                motivoCancelamento = MotivoCancelamento.MEDICO_CANCELOU,
            )
        val consultaId = consulta.id!!
        val dadosCancelamento = TestFixtures.dadosCancelamentoConsulta(idConsulta = consultaId)

        whenever(consultaRepository.findById(consultaId)).thenReturn(consulta)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.cancelar(dadosCancelamento)
            }

        assertEquals("Consulta já está cancelada.", exception.message)
        verify(consultaRepository, never()).save(any<Consulta>())
    }

    @Test
    fun `deve lancar excecao ao cancelar consulta com menos de 24 horas`() {
        val data = LocalDateTime.now().plusHours(1).withSecond(0).withNano(0)
        val consulta = TestFixtures.consulta(data = data)
        val consultaId = consulta.id!!
        val dadosCancelamento = TestFixtures.dadosCancelamentoConsulta(idConsulta = consultaId)

        whenever(consultaRepository.findById(consultaId)).thenReturn(consulta)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.cancelar(dadosCancelamento)
            }

        assertEquals(
            "Consulta só pode ser cancelada com antecedência mínima de 24 horas.",
            exception.message,
        )
        verify(consultaRepository, never()).save(any<Consulta>())
    }

    @Test
    fun `deve lancar excecao quando horario de agendamento for antes das 7h`() {
        val data = dataFutura(hora = 6)
        val dadosAgendamento = TestFixtures.dadosAgendamentoConsulta(data = data)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals(
            "Consulta fora do horário de funcionamento da clínica (Seg-Sáb, 07:00 às 18:00).",
            exception.message,
        )
    }

    @Test
    fun `deve permitir agendamento as 7h`() {
        val data = dataFutura(hora = 7)
        val medico = TestFixtures.medico()
        val medicoId = medico.id!!
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idMedico = medicoId,
                idPaciente = pacienteId,
                data = data,
            )
        val consultaEsperada = TestFixtures.consulta(data = data, medico = medico, paciente = paciente)

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(medicoRepository.findById(medicoId)).thenReturn(medico)
        whenever(
            consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medicoId, data),
        ).thenReturn(false)
        whenever(
            consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
                pacienteId,
                data.withHour(7),
                data.withHour(18),
            ),
        ).thenReturn(false)
        whenever(consultaRepository.save(any())).thenReturn(consultaEsperada)

        val resultado = consultaUseCase.agendar(dadosAgendamento)

        assertEquals(medicoId, resultado.idMedico)
        assertEquals(pacienteId, resultado.idPaciente)
    }

    @Test
    fun `deve lancar excecao quando especialidade nao for informada com medico aleatorio`() {
        val data = dataFutura()
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idMedico = null,
                idPaciente = pacienteId,
                data = data,
                especialidade = null,
            )

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals("Especialidade é obrigatória quando médico não for escolhido.", exception.message)
    }

    @Test
    fun `deve lancar excecao quando nao houver medico disponivel para especialidade e data`() {
        val data = dataFutura()
        val paciente = TestFixtures.paciente()
        val pacienteId = paciente.id!!
        val especialidade = Especialidade.CARDIOLOGIA
        val dadosAgendamento =
            TestFixtures.dadosAgendamentoConsulta(
                idMedico = null,
                idPaciente = pacienteId,
                data = data,
                especialidade = especialidade,
            )

        whenever(pacienteRepository.findById(pacienteId)).thenReturn(paciente)
        whenever(
            medicoRepository.escolherMedicoAleatorioLivreNaData(especialidade, data),
        ).thenReturn(null)

        val exception =
            assertThrows(IllegalStateException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals(
            "Nenhum médico disponível para a especialidade e data informados.",
            exception.message,
        )
    }

    @Test
    fun `deve lancar excecao quando paciente nao for encontrado`() {
        val data = dataFutura()
        val dadosAgendamento = TestFixtures.dadosAgendamentoConsulta(data = data)

        whenever(pacienteRepository.findById(dadosAgendamento.idPaciente)).thenReturn(null)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                consultaUseCase.agendar(dadosAgendamento)
            }

        assertEquals("Paciente não encontrado.", exception.message)
    }

    @Test
    fun `deve lancar excecao ao detalhar consulta inexistente`() {
        whenever(consultaRepository.findById(999L)).thenReturn(null)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                consultaUseCase.detalhar(999L)
            }

        assertEquals("Consulta não encontrada.", exception.message)
    }

    @Test
    fun `deve lancar excecao ao cancelar consulta inexistente`() {
        val dadosCancelamento = TestFixtures.dadosCancelamentoConsulta(idConsulta = 999L)

        whenever(consultaRepository.findById(999L)).thenReturn(null)

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                consultaUseCase.cancelar(dadosCancelamento)
            }

        assertEquals("Consulta não encontrada.", exception.message)
    }
}
