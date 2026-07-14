package com.med.scheduler.integration

import com.med.scheduler.application.dto.DadosCadastroMedico
import com.med.scheduler.domain.model.enums.Especialidade
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class MedicoIntegrationTest : IntegrationTestBase() {
    @Test
    fun `deve cadastrar e listar medico`() {
        val dados =
            DadosCadastroMedico(
                nome = "Medico Teste",
                email = "medico${System.nanoTime()}@teste.com",
                telefone = "11999999999",
                crm = "123456",
                especialidade = Especialidade.CARDIOLOGIA,
                endereco =
                    com.med.scheduler.application.dto.DadosEndereco(
                        logradouro = "Rua A",
                        bairro = "Bairro",
                        cep = "12345678",
                        numero = "10",
                        complemento = null,
                        cidade = "Cidade",
                        uf = "SP",
                    ),
            )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/medicos")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)),
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(dados.nome))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/medicos")
                .header("Authorization", "Bearer $token"),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}
