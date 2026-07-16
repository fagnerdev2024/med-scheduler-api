package com.med.scheduler.integration

import com.fasterxml.jackson.databind.JsonNode
import com.med.scheduler.application.dto.DadosAtualizacaoMedicoRequest
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
                crm = System.nanoTime().toString().takeLast(6),
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

    @Test
    fun `deve atualizar medico`() {
        val cadastro =
            DadosCadastroMedico(
                nome = "Dr. Atualizar",
                email = "atualizar${System.nanoTime()}@teste.com",
                telefone = "11999999999",
                crm = System.nanoTime().toString().takeLast(6),
                especialidade = Especialidade.CARDIOLOGIA,
                endereco =
                    com.med.scheduler.application.dto.DadosEndereco(
                        logradouro = "Rua A",
                        bairro = "Bairro",
                        cep = "12345678",
                        cidade = "Cidade",
                        uf = "SP",
                        numero = "100",
                        complemento = null,
                    ),
            )

        val resultado =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/medicos")
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cadastro)),
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()

        val resposta: JsonNode = objectMapper.readTree(resultado.response.contentAsString)
        val id = resposta.get("id").asLong()

        val atualizacao =
            DadosAtualizacaoMedicoRequest(
                nome = "Dr. Atualizado",
                telefone = "11888888888",
                endereco = cadastro.endereco.copy(logradouro = "Rua B"),
            )

        mockMvc.perform(
            MockMvcRequestBuilders.put("/medicos/$id")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizacao)),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Dr. Atualizado"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.logradouro").value("Rua B"))
    }
}
