package com.med.scheduler.domain.repository

import com.med.scheduler.domain.model.Paciente
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PacienteRepository {
    fun save(paciente: Paciente): Paciente

    fun findById(id: Long): Paciente?

    fun findAllByAtivoTrue(paginacao: Pageable): Page<Paciente>

    fun existsByEmail(email: String): Boolean

    fun findAtivoById(id: Long): Boolean?

    fun delete(id: Long)
}
