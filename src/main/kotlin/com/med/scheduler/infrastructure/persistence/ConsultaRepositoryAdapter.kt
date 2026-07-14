package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Consulta
import com.med.scheduler.domain.repository.ConsultaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ConsultaRepositoryAdapter(
    private val jpaRepository: ConsultaJpaRepository,
) : ConsultaRepository {
    override fun save(consulta: Consulta): Consulta {
        return jpaRepository.save(consulta)
    }

    override fun findById(id: Long): Consulta? {
        return jpaRepository.findById(id).orElse(null)
    }

    override fun findAll(paginacao: Pageable): Page<Consulta> {
        return jpaRepository.findAll(paginacao)
    }

    override fun existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
        pacienteId: Long,
        inicio: LocalDateTime,
        fim: LocalDateTime,
    ): Boolean {
        return jpaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(
            pacienteId,
            inicio,
            fim,
        )
    }

    override fun existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(
        medicoId: Long,
        data: LocalDateTime,
    ): Boolean {
        return jpaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medicoId, data)
    }

    override fun delete(id: Long) {
        jpaRepository.deleteById(id)
    }
}
