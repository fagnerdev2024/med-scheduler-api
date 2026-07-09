package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Paciente
import com.med.scheduler.domain.repository.PacienteRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class PacienteRepositoryAdapter(
    private val jpaRepository: PacienteJpaRepository
) : PacienteRepository {
    override fun save(paciente: Paciente): Paciente {
        return jpaRepository.save(paciente)
    }

    override fun findById(id: Long): Paciente? {
        return jpaRepository.findById(id).orElse(null)
    }

    override fun findAll(paginacao: Pageable): Page<Paciente> {
        return jpaRepository.findAll(paginacao)
    }

    override fun existsByEmail(email: String): Boolean {
        return jpaRepository.existsByEmail(email)
    }

    override fun delete(id: Long) {
        jpaRepository.deleteById(id)
    }
}