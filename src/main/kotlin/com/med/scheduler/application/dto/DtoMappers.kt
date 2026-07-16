package com.med.scheduler.application.dto

import com.med.scheduler.domain.model.Consulta
import com.med.scheduler.domain.model.Endereco
import com.med.scheduler.domain.model.Medico
import com.med.scheduler.domain.model.Paciente

fun DadosEndereco.toEndereco() =
    Endereco(
        logradouro = this.logradouro,
        bairro = this.bairro,
        cep = this.cep,
        numero = this.numero,
        complemento = this.complemento,
        cidade = this.cidade,
        uf = this.uf,
    )

fun DadosAtualizacaoMedicoRequest.toUseCase(id: Long) =
    DadosAtualizacaoMedico(
        id = id,
        nome = this.nome,
        telefone = this.telefone,
        endereco = this.endereco,
    )

fun DadosAtualizacaoPacienteRequest.toUseCase(id: Long) =
    DadosAtualizacaoPaciente(
        id = id,
        nome = this.nome,
        telefone = this.telefone,
        endereco = this.endereco,
    )

fun Endereco.toDadosEndereco() =
    DadosEndereco(
        logradouro = this.logradouro,
        bairro = this.bairro,
        cep = this.cep,
        numero = this.numero,
        complemento = this.complemento,
        cidade = this.cidade,
        uf = this.uf,
    )

fun Medico.toListagemDTO() =
    DadosListagemMedico(
        id = this.id,
        nome = this.nome,
        email = this.email,
        crm = this.crm,
        especialidade = this.especialidade,
        ativo = this.ativo,
    )

fun Medico.toDetalhamentoDTO() =
    DadosDetalhamentoMedico(
        id = this.id,
        nome = this.nome,
        email = this.email,
        crm = this.crm,
        telefone = this.telefone,
        especialidade = this.especialidade,
        endereco = this.endereco.toDadosEndereco(),
        ativo = this.ativo,
    )

fun Paciente.toListagemDTO() =
    DadosListagemPaciente(
        id = this.id,
        nome = this.nome,
        email = this.email,
        cpf = this.cpf,
        ativo = this.ativo,
    )

fun Paciente.toDetalhamentoDTO() =
    DadosDetalhamentoPaciente(
        id = this.id,
        nome = this.nome,
        email = this.email,
        cpf = this.cpf,
        telefone = this.telefone,
        endereco = this.endereco.toDadosEndereco(),
        ativo = this.ativo,
    )

fun Consulta.toDetalhamentoDTO() =
    DadosDetalhamentoConsulta(
        id = this.id,
        idMedico = this.medico.id,
        idPaciente = this.paciente.id,
        data = this.data,
        motivoCancelamento = this.motivoCancelamento,
    )
