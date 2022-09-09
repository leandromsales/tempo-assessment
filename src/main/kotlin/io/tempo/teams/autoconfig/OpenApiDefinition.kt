package io.tempo.teams.autoconfig

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiDefinition {

    @Bean
    fun customOpenAPI() : OpenAPI {
        return OpenAPI()
            .components(Components().addSecuritySchemes("basicScheme", SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
            .info(Info()
                .title("Tempo Service API")
                .version("1.0")
                .description("This is the Tempo API documentation created during the interview process for a software engineer position at Tempo Software.")
                .termsOfService("http://swagger.io/terms/")
                .license(License().name("Apache 2.0").url("http://springdoc.org")));
    }
}