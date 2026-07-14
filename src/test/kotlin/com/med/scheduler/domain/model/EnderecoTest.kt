package com.med.scheduler.domain.model

import com.med.scheduler.TestFixtures
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EnderecoTest {
    @Test
    fun `deve manter valores originais quando atualizar com nulos`() {
        val endereco = TestFixtures.endereco()

        endereco.atualizarInformacoes(null, null, null, null, null, null, null)

        assertEquals("Rua Teste", endereco.logradouro)
        assertEquals("Bairro Teste", endereco.bairro)
        assertEquals("12345678", endereco.cep)
        assertEquals("100", endereco.numero)
        assertEquals("Apto 1", endereco.complemento)
        assertEquals("Cidade Teste", endereco.cidade)
        assertEquals("SP", endereco.uf)
    }

    @Test
    fun `deve atualizar valores quando informados`() {
        val endereco = TestFixtures.endereco()

        endereco.atualizarInformacoes(
            logradouro = "Rua Nova",
            bairro = "Bairro Novo",
            cep = "87654321",
            numero = "200",
            complemento = "Casa 2",
            cidade = "Nova Cidade",
            uf = "RJ",
        )

        assertEquals("Rua Nova", endereco.logradouro)
        assertEquals("Bairro Novo", endereco.bairro)
        assertEquals("87654321", endereco.cep)
        assertEquals("200", endereco.numero)
        assertEquals("Casa 2", endereco.complemento)
        assertEquals("Nova Cidade", endereco.cidade)
        assertEquals("RJ", endereco.uf)
    }

    @Test
    fun `deve ser igual quando conteudo for igual`() {
        val endereco1 = TestFixtures.endereco()
        val endereco2 = TestFixtures.endereco()

        assertEquals(endereco1, endereco2)
        assertEquals(endereco1.hashCode(), endereco2.hashCode())
    }

    @Test
    fun `nao deve ser igual quando conteudo for diferente`() {
        val endereco1 = TestFixtures.endereco()
        val endereco2 = TestFixtures.endereco(logradouro = "Outra Rua")

        assertNotEquals(endereco1, endereco2)
    }

    @Test
    fun `deve permitir complemento nulo em equals e hashCode`() {
        val endereco1 = TestFixtures.endereco(complemento = null)
        val endereco2 = TestFixtures.endereco(complemento = null)

        assertEquals(endereco1, endereco2)
        assertEquals(endereco1.hashCode(), endereco2.hashCode())
    }

    @Test
    fun `nao deve ser igual a outro tipo`() {
        val endereco = TestFixtures.endereco()

        assertNotEquals(endereco, "endereco")
    }
}
