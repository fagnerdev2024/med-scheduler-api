package com.med.scheduler.domain.model

import jakarta.persistence.*
import com.med.scheduler.domain.model.enums.Especialidade

@Table(name = "medicos")
@Entity(name = "Medico")
class Medico(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var nome: String,

    var email: String,

    var telefone: String,

    var crm: String,

    @Enumerated(EnumType.STRING)
    var especialidade: Especialidade,

    @Embedded
    var endereco: Endereco,

    var ativo: Boolean = true
) {
    fun atualizarInformacoes(nome: String?, telefone: String?, endereco: Endereco?) {
        nome?.let { this.nome = it }
        telefone?.let { this.telefone = it }
        endereco?.let { this.endereco = it }
    }

    fun atualizarInformacoesComEnderecoDetalhado(
        nome: String?,
        telefone: String?,
        logradouro: String?,
        bairro: String?,
        cep: String?,
        numero: String?,
        complemento: String?,
        cidade: String?,
        uf: String?
    ) {
        nome?.let { this.nome = it }
        telefone?.let { this.telefone = it }
        this.endereco.atualizarInformacoes(logradouro, bairro, cep, numero, complemento, cidade, uf)
    }

    fun excluir() {
        this.ativo = false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Medico) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}