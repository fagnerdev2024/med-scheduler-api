package com.med.scheduler.domain.service

import org.springframework.security.core.userdetails.UserDetails

interface TokenService {
    fun gerarToken(userDetails: UserDetails): String
    fun getSubject(token: String): String?
}
