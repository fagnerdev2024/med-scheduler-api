package com.med.scheduler.domain.repository

import com.med.scheduler.domain.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UserDetails

interface UsuarioRepository : JpaRepository<Usuario, Long> {

    fun findByLogin(login: String): UserDetails?
}
