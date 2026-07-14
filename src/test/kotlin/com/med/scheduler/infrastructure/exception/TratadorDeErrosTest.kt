package com.med.scheduler.infrastructure.exception

import com.med.scheduler.domain.exception.ValidacaoException
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.core.MethodParameter
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException

class TratadorDeErrosTest {
    private val handler = TratadorDeErros()

    private fun criarMethodArgumentNotValidException(): MethodArgumentNotValidException {
        class Dummy {
            fun method(param: Any) {}
        }
        val method = Dummy::class.java.getDeclaredMethod("method", Any::class.java)
        val parameter = MethodParameter(method, 0)
        val bindingResult = BeanPropertyBindingResult(Dummy(), "dummy")
        bindingResult.addError(FieldError("dummy", "nome", "não pode ser vazio"))
        return MethodArgumentNotValidException(parameter, bindingResult)
    }

    @Test
    fun `deve retornar 404 para EntityNotFoundException`() {
        val response = handler.tratarErro404(EntityNotFoundException("Não encontrado"))

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `deve retornar 400 para MethodArgumentNotValidException`() {
        val response = handler.tratarErro400(criarMethodArgumentNotValidException())

        assertEquals(400, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals("nome", response.body?.first()?.get("campo"))
        assertEquals("não pode ser vazio", response.body?.first()?.get("mensagem"))
    }

    @Test
    fun `deve retornar mensagem vazia quando FieldError nao tiver defaultMessage`() {
        class Dummy {
            fun method(param: Any) {}
        }
        val method = Dummy::class.java.getDeclaredMethod("method", Any::class.java)
        val parameter = MethodParameter(method, 0)
        val bindingResult = BeanPropertyBindingResult(Dummy(), "dummy")
        val fieldError =
            FieldError::class.java
                .getConstructor(String::class.java, String::class.java, String::class.java)
                .newInstance("dummy", "nome", null)
        bindingResult.addError(fieldError)
        val ex = MethodArgumentNotValidException(parameter, bindingResult)

        val response = handler.tratarErro400(ex)

        assertEquals(400, response.statusCode.value())
        assertEquals("", response.body?.first()?.get("mensagem"))
    }

    @Test
    fun `deve retornar 400 para HttpMessageNotReadableException`() {
        val response = handler.tratarErro400(HttpMessageNotReadableException("JSON inválido"))

        assertEquals(400, response.statusCode.value())
        assertTrue(response.body!!.contains("JSON inválido"))
    }

    @Test
    fun `deve retornar 400 para ValidacaoException`() {
        val response = handler.tratarErroRegraDeNegocio(ValidacaoException("Regra violada"))

        assertEquals(400, response.statusCode.value())
        assertEquals("Regra violada", response.body)
    }

    @Test
    fun `deve retornar 400 para IllegalArgumentException`() {
        val response = handler.tratarErroRegraDeNegocio(IllegalArgumentException("Argumento inválido"))

        assertEquals(400, response.statusCode.value())
        assertEquals("Argumento inválido", response.body)
    }

    @Test
    fun `deve retornar 400 para IllegalStateException`() {
        val response = handler.tratarErroRegraDeNegocio(IllegalStateException("Estado inválido"))

        assertEquals(400, response.statusCode.value())
        assertEquals("Estado inválido", response.body)
    }

    @Test
    fun `deve retornar 401 para BadCredentialsException`() {
        val response = handler.tratarErroBadCredentials(BadCredentialsException("Credenciais inválidas"))

        assertEquals(401, response.statusCode.value())
        assertEquals("Credenciais inválidas", response.body)
    }

    @Test
    fun `deve retornar 401 para AuthenticationException`() {
        val response = handler.tratarErroAuthentication(AuthenticationCredentialsNotFoundException("Autenticação necessária"))

        assertEquals(401, response.statusCode.value())
        assertEquals("Falha na autenticação", response.body)
    }

    @Test
    fun `deve retornar 403 para AccessDeniedException`() {
        val response = handler.tratarErroAcessoNegado(AccessDeniedException("Acesso negado"))

        assertEquals(403, response.statusCode.value())
        assertEquals("Acesso negado", response.body)
    }

    @Test
    fun `deve retornar 500 para Exception generica`() {
        val response = handler.tratarErro500(RuntimeException("Erro interno"))

        assertEquals(500, response.statusCode.value())
        assertTrue(response.body!!.contains("Erro interno"))
    }
}
