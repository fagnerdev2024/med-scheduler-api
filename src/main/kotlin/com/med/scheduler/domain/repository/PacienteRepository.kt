package com.med.scheduler.domain.repository

import com.med.scheduler.domain.model.Paciente
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PacienteRepository {
    fun save(paciente: Paciente): Paciente
    fun findById(id: Long): Paciente?
    fun findAll(paginacao: Pageable): Page<Paciente>
    fun findByAtivoTrue(id: Long): Boolean?
    fun delete(id: Long)
}