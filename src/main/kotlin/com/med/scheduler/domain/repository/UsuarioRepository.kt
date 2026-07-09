package com.med.scheduler.domain.repository

import com.med.scheduler.domain.model.Usuario

interface UsuarioRepository {
    fun save(usuario: Usuario): Usuario
    fun findByLogin(login: String): Usuario?
}
