package com.med.scheduler.domain.model

import jakarta.persistence.*
import com.med.scheduler.domain.model.enums.MotivoCancelamento
import java.time.LocalDateTime

@Table(name = "consultas")
@Entity(name = "Consulta")
class Consulta(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    var medico: Medico,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    var paciente: Paciente,

    var data: LocalDateTime,

    @Column(name = "motivo_cancelamento")
    @Enumerated(EnumType.STRING)
    var motivoCancelamento: MotivoCancelamento? = null
) {
    fun cancelar(motivoCancelamento: MotivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Consulta) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
