package com.med.scheduler

import com.med.scheduler.application.dto.DadosAgendamentoConsulta
import com.med.scheduler.application.dto.DadosCadastroMedico
import com.med.scheduler.application.dto.DadosCadastroPaciente
import com.med.scheduler.application.dto.DadosCancelamentoConsulta
import com.med.scheduler.application.dto.DadosEndereco
import com.med.scheduler.domain.model.Consulta
import com.med.scheduler.domain.model.Endereco
import com.med.scheduler.domain.model.Medico
import com.med.scheduler.domain.model.Paciente
import com.med.scheduler.domain.model.enums.Especialidade
import com.med.scheduler.domain.model.enums.MotivoCancelamento
import java.time.LocalDateTime

object TestFixtures {

    fun endereco(
        logradouro: String = "Rua Teste",
        bairro: String = "Bairro Teste",
        cep: String = "12345678",
        numero: String = "100",
        complemento: String? = "Apto 1",
        cidade: String = "Cidade Teste",
        uf: String = "SP"
    ) = Endereco(
        logradouro = logradouro,
        bairro = bairro,
        cep = cep,
        numero = numero,
        complemento = complemento,
        cidade = cidade,
        uf = uf
    )

    fun dadosEndereco(
        logradouro: String = "Rua Teste",
        bairro: String = "Bairro Teste",
        cep: String = "12345678",
        cidade: String = "Cidade Teste",
        uf: String = "SP",
        complemento: String? = "Apto 1",
        numero: String = "100"
    ) = DadosEndereco(
        logradouro = logradouro,
        bairro = bairro,
        cep = cep,
        cidade = cidade,
        uf = uf,
        complemento = complemento,
        numero = numero
    )

    fun medico(
        id: Long? = 1L,
        nome: String = "Dr. Teste",
        email: String = "medico@teste.com",
        telefone: String = "11999999999",
        crm: String = "1234",
        especialidade: Especialidade = Especialidade.CARDIOLOGIA,
        endereco: Endereco = endereco(),
        ativo: Boolean = true
    ) = Medico(
        id = id,
        nome = nome,
        email = email,
        telefone = telefone,
        crm = crm,
        especialidade = especialidade,
        endereco = endereco,
        ativo = ativo
    )

    fun dadosCadastroMedico(
        nome: String = "Dr. Teste",
        email: String = "medico@teste.com",
        telefone: String = "11999999999",
        crm: String = "1234",
        especialidade: Especialidade = Especialidade.CARDIOLOGIA,
        endereco: DadosEndereco = dadosEndereco()
    ) = DadosCadastroMedico(
        nome = nome,
        email = email,
        telefone = telefone,
        crm = crm,
        especialidade = especialidade,
        endereco = endereco
    )

    fun paciente(
        id: Long? = 1L,
        nome: String = "Paciente Teste",
        email: String = "paciente@teste.com",
        cpf: String = "123.456.789-09",
        telefone: String = "11988888888",
        endereco: Endereco = endereco(),
        ativo: Boolean = true
    ) = Paciente(
        id = id,
        nome = nome,
        email = email,
        cpf = cpf,
        telefone = telefone,
        endereco = endereco,
        ativo = ativo
    )

    fun dadosCadastroPaciente(
        nome: String = "Paciente Teste",
        email: String = "paciente@teste.com",
        telefone: String = "11988888888",
        cpf: String = "123.456.789-09",
        endereco: DadosEndereco = dadosEndereco()
    ) = DadosCadastroPaciente(
        nome = nome,
        email = email,
        telefone = telefone,
        cpf = cpf,
        endereco = endereco
    )

    fun consulta(
        id: Long? = 1L,
        medico: Medico = medico(),
        paciente: Paciente = paciente(),
        data: LocalDateTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
        motivoCancelamento: MotivoCancelamento? = null
    ) = Consulta(
        id = id,
        medico = medico,
        paciente = paciente,
        data = data,
        motivoCancelamento = motivoCancelamento
    )

    fun dadosAgendamentoConsulta(
        idMedico: Long? = 1L,
        idPaciente: Long = 1L,
        data: LocalDateTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
        especialidade: Especialidade? = null
    ) = DadosAgendamentoConsulta(
        idMedico = idMedico,
        idPaciente = idPaciente,
        data = data,
        especialidade = especialidade
    )

    fun dadosCancelamentoConsulta(
        idConsulta: Long = 1L,
        motivo: MotivoCancelamento = MotivoCancelamento.PACIENTE_DESISTIU
    ) = DadosCancelamentoConsulta(
        idConsulta = idConsulta,
        motivo = motivo
    )
}
