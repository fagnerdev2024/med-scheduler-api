package com.med.scheduler.domain.model.enums

enum class MotivoCancelamento(val descricao: String) {
    PACIENTE_DESISTIU("Paciente desistiu"),
    MEDICO_CANCELOU("Médico cancelou"),
    OUTROS("Outros motivos");

    override fun toString(): String {
        return "$name - $descricao"
    }
}
