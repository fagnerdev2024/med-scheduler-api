package com.med.scheduler.infrastructure.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringDocConfigurations {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearer-key",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
            .info(
                Info()
                    .title("Voll.med API")
                    .description("API Rest da aplicação Voll.med, com CRUD de médicos e pacientes, agendamento e cancelamento de consultas.")
                    .contact(
                        Contact()
                            .name("Time Backend")
                            .email("backend@voll.med")
                    )
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("http://voll.med/api/licenca")
                    )
            )
    }
}
