package com.med.scheduler.application.dto

import com.med.scheduler.TestFixtures
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DtoMappersTest {
    @Test
    fun `deve converter DadosEndereco para Endereco`() {
        val dto = TestFixtures.dadosEndereco(complemento = null)

        val endereco = dto.toEndereco()

        assertEquals(dto.logradouro, endereco.logradouro)
        assertEquals(dto.bairro, endereco.bairro)
        assertEquals(dto.cep, endereco.cep)
        assertEquals(dto.numero, endereco.numero)
        assertNull(endereco.complemento)
        assertEquals(dto.cidade, endereco.cidade)
        assertEquals(dto.uf, endereco.uf)
    }

    @Test
    fun `deve converter Endereco para DadosEndereco`() {
        val endereco = TestFixtures.endereco()

        val dto = endereco.toDadosEndereco()

        assertEquals(endereco.logradouro, dto.logradouro)
        assertEquals(endereco.bairro, dto.bairro)
        assertEquals(endereco.cep, dto.cep)
        assertEquals(endereco.numero, dto.numero)
        assertEquals(endereco.complemento, dto.complemento)
        assertEquals(endereco.cidade, dto.cidade)
        assertEquals(endereco.uf, dto.uf)
    }

    @Test
    fun `deve converter Medico para DadosListagemMedico`() {
        val medico = TestFixtures.medico()

        val dto = medico.toListagemDTO()

        assertEquals(medico.id, dto.id)
        assertEquals(medico.nome, dto.nome)
        assertEquals(medico.email, dto.email)
        assertEquals(medico.crm, dto.crm)
        assertEquals(medico.especialidade, dto.especialidade)
        assertEquals(medico.ativo, dto.ativo)
    }

    @Test
    fun `deve converter Medico para DadosDetalhamentoMedico`() {
        val medico = TestFixtures.medico()

        val dto = medico.toDetalhamentoDTO()

        assertEquals(medico.id, dto.id)
        assertEquals(medico.nome, dto.nome)
        assertEquals(medico.email, dto.email)
        assertEquals(medico.crm, dto.crm)
        assertEquals(medico.telefone, dto.telefone)
        assertEquals(medico.especialidade, dto.especialidade)
        assertEquals(medico.endereco.logradouro, dto.endereco.logradouro)
        assertEquals(medico.ativo, dto.ativo)
    }

    @Test
    fun `deve converter Paciente para DadosListagemPaciente`() {
        val paciente = TestFixtures.paciente()

        val dto = paciente.toListagemDTO()

        assertEquals(paciente.id, dto.id)
        assertEquals(paciente.nome, dto.nome)
        assertEquals(paciente.email, dto.email)
        assertEquals(paciente.cpf, dto.cpf)
        assertEquals(paciente.ativo, dto.ativo)
    }

    @Test
    fun `deve converter Paciente para DadosDetalhamentoPaciente`() {
        val paciente = TestFixtures.paciente()

        val dto = paciente.toDetalhamentoDTO()

        assertEquals(paciente.id, dto.id)
        assertEquals(paciente.nome, dto.nome)
        assertEquals(paciente.email, dto.email)
        assertEquals(paciente.cpf, dto.cpf)
        assertEquals(paciente.telefone, dto.telefone)
        assertEquals(paciente.endereco.logradouro, dto.endereco.logradouro)
        assertEquals(paciente.ativo, dto.ativo)
    }

    @Test
    fun `deve converter DadosAtualizacaoMedicoRequest para DadosAtualizacaoMedico`() {
        val request = TestFixtures.dadosAtualizacaoMedicoRequest()
        val id = 2L

        val dto = request.toUseCase(id)

        assertEquals(id, dto.id)
        assertEquals(request.nome, dto.nome)
        assertEquals(request.telefone, dto.telefone)
        assertEquals(request.endereco, dto.endereco)
    }

    @Test
    fun `deve converter DadosAtualizacaoPacienteRequest para DadosAtualizacaoPaciente`() {
        val request = TestFixtures.dadosAtualizacaoPacienteRequest()
        val id = 3L

        val dto = request.toUseCase(id)

        assertEquals(id, dto.id)
        assertEquals(request.nome, dto.nome)
        assertEquals(request.telefone, dto.telefone)
        assertEquals(request.endereco, dto.endereco)
    }

    @Test
    fun `deve converter Consulta para DadosDetalhamentoConsulta`() {
        val consulta = TestFixtures.consulta()

        val dto = consulta.toDetalhamentoDTO()

        assertEquals(consulta.id, dto.id)
        assertEquals(consulta.medico.id, dto.idMedico)
        assertEquals(consulta.paciente.id, dto.idPaciente)
        assertEquals(consulta.data, dto.data)
        assertEquals(consulta.motivoCancelamento, dto.motivoCancelamento)
    }
}
