package com.med.scheduler.domain.model.enums

enum class Especialidade(val descricao: String) {
    ORTOPEDIA("Tratamento de condições do sistema musculoesquelético, incluindo ossos, músculos e articulações."),
    CARDIOLOGIA("Diagnóstico e tratamento de doenças do coração e do sistema cardiovascular."),
    GINECOLOGIA("Cuidados médicos relacionados à saúde feminina, incluindo o sistema reprodutivo."),
    DERMATOLOGIA("Estudo e tratamento de condições relacionadas à pele, cabelos e unhas."),
    NEUROLOGIA("Diagnóstico e tratamento de distúrbios do sistema nervoso, incluindo cérebro e medula espinhal."),
    PEDIATRIA("Cuidados médicos voltados para crianças e adolescentes, abrangendo crescimento e desenvolvimento."),
    ;

    override fun toString(): String {
        return "$name - $descricao"
    }
}
