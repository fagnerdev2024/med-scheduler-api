package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Paciente
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PacienteJpaRepository : JpaRepository<Paciente, Long> {
    fun existsByEmail(email: String): Boolean
}