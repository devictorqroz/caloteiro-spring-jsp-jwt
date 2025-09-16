package com.caloteiros.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String apiDescription = "Documentação da API REST do sistema Caloteiros." +
                "<br><br><b>Para testar os endpoints, utilize o usuário de demonstração criado " +
                "automaticamente no ambiente de desenvolvimento:</b>" +
                "<ul>" +
                "<li><b>Email:</b> `demo@caloteiros.com`</li>" +
                "<li><b>Senha:</b> `password`</li>" +
                "</ul>" +
                "Use essas credenciais no endpoint `/api/auth/login` para obter um token JWT e " +
                "use o botão 'Authorize' para autenticar suas requisições.";

        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Caloteiros API")
                        .version("1.0")
                        .description(apiDescription));

        Components components = new Components()
                .addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

        openAPI.setComponents(components);
        openAPI.addSecurityItem(securityRequirement);

        return openAPI;

    }
}