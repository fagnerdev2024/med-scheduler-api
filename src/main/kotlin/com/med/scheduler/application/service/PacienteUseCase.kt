package com.med.scheduler.application.service

import com.med.scheduler.application.dto.*
import com.med.scheduler.domain.model.Paciente
import com.med.scheduler.domain.repository.PacienteRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PacienteUseCase(
    private val pacienteRepository: PacienteRepository
) {
    private val log = LoggerFactory.getLogger(PacienteUseCase::class.java)
    private val MAX_PAGE_SIZE = 100

    @Transactional
    fun cadastrar(dadosCadastroPaciente: DadosCadastroPaciente): DadosDetalhamentoPaciente {
        log.info("Iniciando cadastro do paciente: {}", dadosCadastroPaciente.nome)
        
        val paciente = Paciente(
            nome = dadosCadastroPaciente.nome,
            email = dadosCadastroPaciente.email,
            telefone = dadosCadastroPaciente.telefone,
            cpf = dadosCadastroPaciente.cpf,
            endereco = dadosCadastroPaciente.endereco.toEndereco(),
            ativo = true
        )
        
        val pacienteSalvo = pacienteRepository.save(paciente)
        log.info("Paciente salvo com sucesso: {}", pacienteSalvo.id)
        
        return pacienteSalvo.toDetalhamentoDTO()
    }

    @Transactional(readOnly = true)
    fun listar(paginacao: Pageable): Page<DadosListagemPaciente> {
        if (paginacao.pageSize > MAX_PAGE_SIZE) {
            throw IllegalArgumentException("O tamanho da página não pode exceder $MAX_PAGE_SIZE registros.")
        }
        
        log.debug("Listando pacientes com paginação: {}", paginacao)
        val pacientes = pacienteRepository.findAll(paginacao)
        
        if (pacientes.isEmpty()) {
            log.debug("Nenhum paciente encontrado.")
            return Page.empty()
        }
        
        return pacientes.map { it.toListagemDTO() }
    }

    @Transactional
    fun atualizar(dadosAtualizacaoPaciente: DadosAtualizacaoPaciente): DadosDetalhamentoPaciente {
        log.info("Iniciando atualização do paciente com ID: {}", dadosAtualizacaoPaciente.id)
        
        val paciente = pacienteRepository.findById(dadosAtualizacaoPaciente.id)
            ?: throw IllegalArgumentException("Paciente com ID ${dadosAtualizacaoPaciente.id} não encontrado.")
        
        paciente.atualizarInformacoes(
            nome = dadosAtualizacaoPaciente.nome,
            telefone = dadosAtualizacaoPaciente.telefone,
            endereco = dadosAtualizacaoPaciente.endereco?.toEndereco()
        )
        
        val pacienteAtualizado = pacienteRepository.save(paciente)
        log.info("Paciente atualizado com sucesso: {}", pacienteAtualizado.id)
        
        return pacienteAtualizado.toDetalhamentoDTO()
    }

    @Transactional
    fun excluir(id: Long) {
        log.info("Iniciando exclusão do paciente com ID: {}", id)
        
        val paciente = pacienteRepository.findById(id)
            ?: throw IllegalArgumentException("Paciente com ID $id não encontrado.")
        
        if (!paciente.ativo) {
            throw IllegalStateException("Paciente com ID $id já está inativo.")
        }
        
        paciente.excluir()
        pacienteRepository.save(paciente)
        log.info("Paciente com ID $id foi excluído logicamente.")
    }

    @Transactional(readOnly = true)
    fun detalhar(id: Long): DadosDetalhamentoPaciente {
        log.info("Iniciando detalhamento do paciente com ID: {}", id)
        
        val paciente = pacienteRepository.findById(id)
            ?: throw IllegalArgumentException("Paciente com ID $id não encontrado.")
        
        if (!paciente.ativo) {
            throw IllegalArgumentException("Paciente com ID $id está inativo.")
        }
        
        return paciente.toDetalhamentoDTO()
    }
}

// Extension functions for DTO conversion
private fun Paciente.toListagemDTO() = DadosListagemPaciente(
    id = this.id,
    nome = this.nome,
    email = this.email,
    cpf = this.cpf,
    ativo = this.ativo
)

private fun Paciente.toDetalhamentoDTO() = DadosDetalhamentoPaciente(
    id = this.id,
    nome = this.nome,
    email = this.email,
    cpf = this.cpf,
    telefone = this.telefone,
    endereco = this.endereco.toDadosEndereco(),
    ativo = this.ativo
)