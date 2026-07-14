package com.med.scheduler.application.service

import com.med.scheduler.application.dto.DadosAutenticacao
import com.med.scheduler.application.dto.DadosTokenJWT
import com.med.scheduler.domain.service.TokenService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AutenticacaoUseCase(
    private val authenticationManager: AuthenticationManager,
    private val tokenService: TokenService,
) {
    fun autenticar(dadosAutenticacao: DadosAutenticacao): DadosTokenJWT {
        val authenticationToken =
            UsernamePasswordAuthenticationToken(
                dadosAutenticacao.login,
                dadosAutenticacao.senha,
            )

        val authentication = authenticationManager.authenticate(authenticationToken)
        val tokenJWT = tokenService.gerarToken(authentication.principal as UserDetails)

        return DadosTokenJWT(tokenJWT)
    }
}
