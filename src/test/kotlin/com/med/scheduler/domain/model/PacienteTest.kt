package com.med.scheduler.domain.model

import com.med.scheduler.TestFixtures
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PacienteTest {
    @Test
    fun `deve atualizar informacoes quando valores nao forem nulos`() {
        val paciente = TestFixtures.paciente()
        val novoEndereco = TestFixtures.endereco(logradouro = "Rua Nova")

        paciente.atualizarInformacoes(nome = "Paciente Novo", telefone = "11999998888", endereco = novoEndereco)

        assertEquals("Paciente Novo", paciente.nome)
        assertEquals("11999998888", paciente.telefone)
        assertEquals(novoEndereco, paciente.endereco)
    }

    @Test
    fun `deve manter informacoes quando atualizar com nulos`() {
        val paciente = TestFixtures.paciente()

        paciente.atualizarInformacoes(nome = null, telefone = null, endereco = null)

        assertEquals("Paciente Teste", paciente.nome)
        assertEquals("11988888888", paciente.telefone)
    }

    @Test
    fun `deve atualizar informacoes com endereco detalhado`() {
        val paciente = TestFixtures.paciente()

        paciente.atualizarInformacoesComEnderecoDetalhado(
            nome = "Paciente Detalhado",
            telefone = "11888888888",
            logradouro = "Rua Detalhada",
            bairro = "Bairro Detalhado",
            cep = "11223344",
            numero = "50",
            complemento = "Sala 10",
            cidade = "Cidade Detalhada",
            uf = "MG",
        )

        assertEquals("Paciente Detalhado", paciente.nome)
        assertEquals("Rua Detalhada", paciente.endereco.logradouro)
        assertEquals("Sala 10", paciente.endereco.complemento)
    }

    @Test
    fun `deve atualizar endereco detalhado sem nome e telefone`() {
        val paciente = TestFixtures.paciente()

        paciente.atualizarInformacoesComEnderecoDetalhado(
            nome = null,
            telefone = null,
            logradouro = "Rua Detalhada",
            bairro = null,
            cep = null,
            numero = null,
            complemento = null,
            cidade = null,
            uf = null,
        )

        assertEquals("Paciente Teste", paciente.nome)
        assertEquals("11988888888", paciente.telefone)
        assertEquals("Rua Detalhada", paciente.endereco.logradouro)
    }

    @Test
    fun `deve permitir alteracao das propriedades restantes`() {
        val paciente = TestFixtures.paciente()

        paciente.email = "novo@example.com"
        paciente.cpf = "98765432100"
        paciente.ativo = true

        assertEquals("novo@example.com", paciente.email)
        assertEquals("98765432100", paciente.cpf)
        assertTrue(paciente.ativo)
    }

    @Test
    fun `deve excluir logicamente`() {
        val paciente = TestFixtures.paciente()

        paciente.excluir()

        assertFalse(paciente.ativo)
    }

    @Test
    fun `deve ter ativo true por padrao`() {
        val paciente =
            Paciente(
                id = null,
                nome = "Paciente Padrão",
                email = "padrao@example.com",
                cpf = "123.456.789-09",
                telefone = "11988888888",
                endereco = TestFixtures.endereco(),
            )

        assertTrue(paciente.ativo)
    }

    @Test
    fun `deve ser igual quando ids forem iguais`() {
        val paciente1 = TestFixtures.paciente(id = 1L, nome = "A")
        val paciente2 = TestFixtures.paciente(id = 1L, nome = "B")

        assertEquals(paciente1, paciente2)
        assertEquals(paciente1.hashCode(), paciente2.hashCode())
    }

    @Test
    fun `nao deve ser igual quando ids forem diferentes`() {
        val paciente1 = TestFixtures.paciente(id = 1L)
        val paciente2 = TestFixtures.paciente(id = 2L)

        assertNotEquals(paciente1, paciente2)
    }

    @Test
    fun `nao deve ser igual quando id for nulo`() {
        val paciente1 = TestFixtures.paciente(id = null)
        val paciente2 = TestFixtures.paciente(id = null)

        assertNotEquals(paciente1, paciente2)
        assertEquals(0, paciente1.hashCode())
    }

    @Test
    fun `nao deve ser igual a outro tipo`() {
        val paciente = TestFixtures.paciente()

        assertNotEquals(paciente, "paciente")
    }
}
