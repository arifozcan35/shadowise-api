package tr.shadowise_api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

@Configuration
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
public class SwaggerConfiguration {
    @Bean
    public GroupedOpenApi allGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";
        return new OpenAPI()
                .info(new Info()
                        .title("Shadowise API")
                        .description("AI-powered content generation and management API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Shadowise Team")
                                .email("support@shadowise.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        .addSchemas("MultipartFile", new Schema<>()
                                .type("string")
                                .format("binary")
                                .description("File upload")));
    }
    
    @Bean
    public GroupedOpenApi aiApiGroup() {
        return GroupedOpenApi.builder()
                .group("AI API")
                .pathsToMatch("/api/ai/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi authApiGroup() {
        return GroupedOpenApi.builder()
                .group("Authentication")
                .pathsToMatch("/api/auth/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi projectApiGroup() {
        return GroupedOpenApi.builder()
                .group("Projects")
                .pathsToMatch("/projects/**")
                .build();
    }
}
