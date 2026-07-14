package com.med.scheduler.infrastructure.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SpringDocConfigurationsTest {
    private val configurations = SpringDocConfigurations()

    @Test
    fun `deve criar OpenAPI com informacoes da API`() {
        val openApi = configurations.customOpenAPI()

        assertNotNull(openApi)
        assertEquals("Med Scheduler API", openApi.info.title)
        assertNotNull(openApi.info.description)
        assertNotNull(openApi.components)
        assertNotNull(openApi.components.securitySchemes["bearer-key"])
        assertEquals("bearer", openApi.components.securitySchemes["bearer-key"]?.scheme)
    }
}
