package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Paciente
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PacienteJpaRepository : JpaRepository<Paciente, Long> {
    fun existsByEmail(email: String): Boolean

    fun findAllByAtivoTrue(paginacao: Pageable): Page<Paciente>

    @Query(
        """
        select p.ativo
        from Paciente p
        where p.id = :id
    """,
    )
    fun findAtivoById(id: Long): Boolean?
}
