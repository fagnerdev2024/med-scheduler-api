package com.med.scheduler.infrastructure.security

import com.med.scheduler.domain.repository.UsuarioRepository
import com.med.scheduler.domain.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class SecurityFilter(
    private val tokenService: TokenService,
    private val usuarioRepository: UsuarioRepository,
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val tokenJWT = recuperarToken(request)

        if (tokenJWT != null) {
            val subject = tokenService.getSubject(tokenJWT)
            if (!subject.isNullOrBlank()) {
                val usuario = usuarioRepository.findByLogin(subject)
                if (usuario != null) {
                    val authentication =
                        UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            usuario.authorities,
                        )
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun recuperarToken(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        return if (!authorizationHeader.isNullOrBlank() && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader.substring(7)
        } else {
            null
        }
    }
}
