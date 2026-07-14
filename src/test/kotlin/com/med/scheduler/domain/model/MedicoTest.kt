package com.med.scheduler.domain.model

import com.med.scheduler.TestFixtures
import com.med.scheduler.domain.model.enums.Especialidade
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MedicoTest {
    @Test
    fun `deve atualizar informacoes quando valores nao forem nulos`() {
        val medico = TestFixtures.medico()
        val novoEndereco = TestFixtures.endereco(logradouro = "Rua Nova")

        medico.atualizarInformacoes(nome = "Dr. Novo", telefone = "11999998888", endereco = novoEndereco)

        assertEquals("Dr. Novo", medico.nome)
        assertEquals("11999998888", medico.telefone)
        assertEquals(novoEndereco, medico.endereco)
    }

    @Test
    fun `deve manter informacoes quando atualizar com nulos`() {
        val medico = TestFixtures.medico()

        medico.atualizarInformacoes(nome = null, telefone = null, endereco = null)

        assertEquals("Dr. Teste", medico.nome)
        assertEquals("11999999999", medico.telefone)
    }

    @Test
    fun `deve atualizar informacoes com endereco detalhado`() {
        val medico = TestFixtures.medico()

        medico.atualizarInformacoesComEnderecoDetalhado(
            nome = "Dr. Detalhado",
            telefone = "11888888888",
            logradouro = "Rua Detalhada",
            bairro = "Bairro Detalhado",
            cep = "11223344",
            numero = "50",
            complemento = "Sala 10",
            cidade = "Cidade Detalhada",
            uf = "MG",
        )

        assertEquals("Dr. Detalhado", medico.nome)
        assertEquals("Rua Detalhada", medico.endereco.logradouro)
        assertEquals("Sala 10", medico.endereco.complemento)
    }

    @Test
    fun `deve atualizar endereco detalhado sem nome e telefone`() {
        val medico = TestFixtures.medico()

        medico.atualizarInformacoesComEnderecoDetalhado(
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

        assertEquals("Dr. Teste", medico.nome)
        assertEquals("11999999999", medico.telefone)
        assertEquals("Rua Detalhada", medico.endereco.logradouro)
    }

    @Test
    fun `deve permitir alteracao das propriedades restantes`() {
        val medico = TestFixtures.medico()

        medico.email = "novo@example.com"
        medico.crm = "999999"
        medico.especialidade = Especialidade.DERMATOLOGIA
        medico.ativo = true

        assertEquals("novo@example.com", medico.email)
        assertEquals("999999", medico.crm)
        assertEquals(Especialidade.DERMATOLOGIA, medico.especialidade)
        assertTrue(medico.ativo)
    }

    @Test
    fun `deve excluir logicamente`() {
        val medico = TestFixtures.medico()

        medico.excluir()

        assertFalse(medico.ativo)
    }

    @Test
    fun `deve ter ativo true por padrao`() {
        val medico =
            Medico(
                id = null,
                nome = "Dr. Padrão",
                email = "padrao@example.com",
                telefone = "11999999999",
                crm = "123456",
                especialidade = Especialidade.CARDIOLOGIA,
                endereco = TestFixtures.endereco(),
            )

        assertTrue(medico.ativo)
    }

    @Test
    fun `deve ser igual quando ids forem iguais`() {
        val medico1 = TestFixtures.medico(id = 1L, nome = "A")
        val medico2 = TestFixtures.medico(id = 1L, nome = "B")

        assertEquals(medico1, medico2)
        assertEquals(medico1.hashCode(), medico2.hashCode())
    }

    @Test
    fun `nao deve ser igual quando ids forem diferentes`() {
        val medico1 = TestFixtures.medico(id = 1L)
        val medico2 = TestFixtures.medico(id = 2L)

        assertNotEquals(medico1, medico2)
    }

    @Test
    fun `nao deve ser igual quando id for nulo`() {
        val medico1 = TestFixtures.medico(id = null)
        val medico2 = TestFixtures.medico(id = null)

        assertNotEquals(medico1, medico2)
        assertEquals(0, medico1.hashCode())
    }

    @Test
    fun `nao deve ser igual a outro tipo`() {
        val medico = TestFixtures.medico()

        assertNotEquals(medico, "medico")
    }
}
