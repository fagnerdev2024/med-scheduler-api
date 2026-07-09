package com.med.scheduler.infrastructure.exception

import com.med.scheduler.domain.exception.ValidacaoException
import jakarta.persistence.EntityNotFoundException
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

    @ExceptionHandler(EntityNotFoundException::class)
    fun tratarErro404(): ResponseEntity<Void> {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun tratarErro400(ex: MethodArgumentNotValidException): ResponseEntity<List<Map<String, String>>> {
        val erros = ex.fieldErrors
        return ResponseEntity.badRequest().body(erros.map { it.toMap() })
    }

    private fun FieldError.toMap(): Map<String, String> {
        return mapOf(
            "campo" to field,
            "mensagem" to (defaultMessage ?: "")
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun tratarErro400(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(ValidacaoException::class, IllegalArgumentException::class, IllegalStateException::class)
    fun tratarErroRegraDeNegocio(ex: RuntimeException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun tratarErroBadCredentials(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas")
    }

    @ExceptionHandler(AuthenticationException::class)
    fun tratarErroAuthentication(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação")
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun tratarErroAcessoNegado(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado")
    }

    @ExceptionHandler(Exception::class)
    fun tratarErro500(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: ${ex.localizedMessage}")
    }
}
