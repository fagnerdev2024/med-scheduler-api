package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Usuario
import com.med.scheduler.domain.repository.UsuarioRepository
import org.springframework.stereotype.Component

@Component
class UsuarioRepositoryAdapter(
    private val jpaRepository: UsuarioJpaRepository
) : UsuarioRepository {
    override fun save(usuario: Usuario): Usuario {
        return jpaRepository.save(usuario)
    }

    override fun findByLogin(login: String): Usuario? {
        return jpaRepository.findByLogin(login)
    }
}