package com.med.scheduler.infrastructure.security

import com.med.scheduler.domain.repository.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AutenticacaoService(
    private val usuarioRepository: UsuarioRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return usuarioRepository.findByLogin(username)
            ?: throw UsernameNotFoundException("Usuário não encontrado: $username")
    }
}
