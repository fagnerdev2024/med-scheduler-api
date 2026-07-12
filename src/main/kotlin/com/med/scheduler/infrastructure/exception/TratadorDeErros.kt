package com.med.scheduler.infrastructure.exception

import com.med.scheduler.domain.exception.ValidacaoException
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class TratadorDeErros {

    private val log = LoggerFactory.getLogger(TratadorDeErros::class.java)

    @ExceptionHandler(EntityNotFoundException::class)
    fun tratarErro404(ex: EntityNotFoundException): ResponseEntity<Void> {
        log.warn("Recurso não encontrado: {}", ex.message)
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun tratarErro400(ex: MethodArgumentNotValidException): ResponseEntity<List<Map<String, String>>> {
        val erros = ex.fieldErrors.map { it.toMap() }
        log.warn("Requisição inválida. Campos: {}", erros)
        return ResponseEntity.badRequest().body(erros)
    }

    private fun FieldError.toMap(): Map<String, String> {
        return mapOf(
            "campo" to field,
            "mensagem" to (defaultMessage ?: "")
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun tratarErro400(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        log.warn("Corpo da requisição não pode ser lido: {}", ex.message)
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(ValidacaoException::class, IllegalArgumentException::class, IllegalStateException::class)
    fun tratarErroRegraDeNegocio(ex: RuntimeException): ResponseEntity<String> {
        log.warn("Erro de regra de negócio: {}", ex.message)
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun tratarErroBadCredentials(ex: BadCredentialsException): ResponseEntity<String> {
        log.warn("Tentativa de login com credenciais inválidas: {}", ex.message)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas")
    }

    @ExceptionHandler(AuthenticationException::class)
    fun tratarErroAuthentication(ex: AuthenticationException): ResponseEntity<String> {
        log.warn("Falha na autenticação: {}", ex.message)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação")
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun tratarErroAcessoNegado(ex: AccessDeniedException): ResponseEntity<String> {
        log.warn("Acesso negado: {}", ex.message)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado")
    }

    @ExceptionHandler(Exception::class)
    fun tratarErro500(ex: Exception): ResponseEntity<String> {
        log.error("Erro interno ao processar requisição: {}", ex.message, ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: ${ex.localizedMessage}")
    }
}
