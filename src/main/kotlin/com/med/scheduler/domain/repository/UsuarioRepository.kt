package com.med.scheduler.domain.repository

import com.med.scheduler.domain.model.Usuario
import org.springframework.security.core.userdetails.UserDetails

interface UsuarioRepository {
    fun save(usuario: Usuario): Usuario
    fun findByLogin(login: String): Usuario?
    fun findByLogin(login: String): UserDetails?
}