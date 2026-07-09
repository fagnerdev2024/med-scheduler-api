package com.med.scheduler.domain.model

import jakarta.persistence.Embeddable

@Embeddable
class Endereco(
    var logradouro: String,
    var bairro: String,
    var cep: String,
    var numero: String,
    var complemento: String?,
    var cidade: String,
    var uf: String
) {
    fun atualizarInformacoes(
        logradouro: String?,
        bairro: String?,
        cep: String?,
        numero: String?,
        complemento: String?,
        cidade: String?,
        uf: String?
    ) {
        logradouro?.let { this.logradouro = it }
        bairro?.let { this.bairro = it }
        cep?.let { this.cep = it }
        numero?.let { this.numero = it }
        complemento?.let { this.complemento = it }
        cidade?.let { this.cidade = it }
        uf?.let { this.uf = it }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Endereco) return false

        return logradouro == other.logradouro &&
                bairro == other.bairro &&
                cep == other.cep &&
                numero == other.numero &&
                complemento == other.complemento &&
                cidade == other.cidade &&
                uf == other.uf
    }

    override fun hashCode(): Int {
        var result = logradouro.hashCode()
        result = 31 * result + bairro.hashCode()
        result = 31 * result + cep.hashCode()
        result = 31 * result + numero.hashCode()
        result = 31 * result + (complemento?.hashCode() ?: 0)
        result = 31 * result + cidade.hashCode()
        result = 31 * result + uf.hashCode()
        return result
    }
}
