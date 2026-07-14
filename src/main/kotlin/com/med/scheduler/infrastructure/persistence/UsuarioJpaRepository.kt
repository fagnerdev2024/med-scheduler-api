package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioJpaRepository : JpaRepository<Usuario, Long> {
    fun findByLogin(login: String): Usuario?
}
