package com.med.scheduler.application.service

import com.med.scheduler.application.dto.*
import com.med.scheduler.domain.model.Medico
import com.med.scheduler.domain.repository.MedicoRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MedicoUseCase(
    private val medicoRepository: MedicoRepository
) {
    private val log = LoggerFactory.getLogger(MedicoUseCase::class.java)
    private val MAX_PAGE_SIZE = 100

    @Transactional
    fun cadastrar(dadosCadastroMedico: DadosCadastroMedico): DadosDetalhamentoMedico {
        log.info("Iniciando cadastro do médico: {}", dadosCadastroMedico.nome)
        
        val medico = Medico(
            nome = dadosCadastroMedico.nome,
            email = dadosCadastroMedico.email,
            telefone = dadosCadastroMedico.telefone,
            crm = dadosCadastroMedico.crm,
            especialidade = dadosCadastroMedico.especialidade,
            endereco = dadosCadastroMedico.endereco.toEndereco(),
            ativo = true
        )
        
        val medicoSalvo = medicoRepository.save(medico)
        log.info("Médico salvo com sucesso: {}", medicoSalvo.id)
        
        return medicoSalvo.toDetalhamentoDTO()
    }

    @Transactional(readOnly = true)
    fun listar(paginacao: Pageable): Page<DadosListagemMedico> {
        if (paginacao.pageSize > MAX_PAGE_SIZE) {
            throw IllegalArgumentException("O tamanho da página não pode exceder $MAX_PAGE_SIZE registros.")
        }
        
        log.debug("Listando médicos ativos com paginação: {}", paginacao)
        val medicos = medicoRepository.findAllByAtivoTrue(paginacao)
        
        if (medicos.isEmpty()) {
            log.debug("Nenhum médico ativo encontrado.")
            return Page.empty()
        }
        
        return medicos.map { it.toListagemDTO() }
    }

    @Transactional
    fun atualizar(dadosAtualizacaoMedico: DadosAtualizacaoMedico): DadosDetalhamentoMedico {
        log.info("Iniciando atualização do médico com ID: {}", dadosAtualizacaoMedico.id)
        
        val medico = medicoRepository.findById(dadosAtualizacaoMedico.id)
            ?: throw IllegalArgumentException("Médico com ID ${dadosAtualizacaoMedico.id} não encontrado.")
        
        medico.atualizarInformacoes(
            nome = dadosAtualizacaoMedico.nome,
            telefone = dadosAtualizacaoMedico.telefone,
            endereco = dadosAtualizacaoMedico.endereco?.toEndereco()
        )
        
        val medicoAtualizado = medicoRepository.save(medico)
        log.info("Médico atualizado com sucesso: {}", medicoAtualizado.id)
        
        return medicoAtualizado.toDetalhamentoDTO()
    }

    @Transactional
    fun excluir(id: Long) {
        log.info("Iniciando exclusão do médico com ID: {}", id)
        
        val medico = medicoRepository.findById(id)
            ?: throw IllegalArgumentException("Médico com ID $id não encontrado.")
        
        if (!medico.ativo) {
            throw IllegalStateException("Médico com ID $id já está inativo.")
        }
        
        medico.excluir()
        medicoRepository.save(medico)
        log.info("Médico com ID $id foi excluído logicamente.")
    }

    @Transactional(readOnly = true)
    fun detalhar(id: Long): DadosDetalhamentoMedico {
        log.info("Iniciando detalhamento do médico com ID: {}", id)
        
        val medico = medicoRepository.findById(id)
            ?: throw IllegalArgumentException("Médico com ID $id não encontrado.")
        
        if (!medico.ativo) {
            throw IllegalArgumentException("Médico com ID $id está inativo.")
        }
        
        return medico.toDetalhamentoDTO()
    }
}

// Extension functions for DTO conversion
private fun DadosEndereco.toEndereco() = com.med.scheduler.domain.model.Endereco(
    logradouro = this.logradouro,
    bairro = this.bairro,
    cep = this.cep,
    numero = this.numero,
    complemento = this.complemento,
    cidade = this.cidade,
    uf = this.uf
)

private fun Medico.toListagemDTO() = DadosListagemMedico(
    id = this.id,
    nome = this.nome,
    email = this.email,
    crm = this.crm,
    especialidade = this.especialidade,
    ativo = this.ativo
)

private fun Medico.toDetalhamentoDTO() = DadosDetalhamentoMedico(
    id = this.id,
    nome = this.nome,
    email = this.email,
    crm = this.crm,
    telefone = this.telefone,
    especialidade = this.especialidade,
    endereco = this.endereco.toDadosEndereco(),
    ativo = this.ativo
)

private fun com.med.scheduler.domain.model.Endereco.toDadosEndereco() = DadosEndereco(
    logradouro = this.logradouro,
    bairro = this.bairro,
    cep = this.cep,
    numero = this.numero,
    complemento = this.complemento,
    cidade = this.cidade,
    uf = this.uf
)