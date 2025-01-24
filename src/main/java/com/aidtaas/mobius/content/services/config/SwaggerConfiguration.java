
package com.aidtaas.mobius.content.services.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SwaggerConfiguration {

  @Value("${swagger.server.url}")
  private String serverUrl;

  @Bean
  public OpenAPI api() {
    log.info("-----creating swagger config bean for groupOpenApi-----");
    return new OpenAPI().info(
            (new Info().title("Mobius-Content-Service").description("Content-Service APIs")
                .version("v1.0").termsOfService("Backened service use only")
                .description("Content-Service controller")))
        .servers(Arrays.asList(new Server().url(serverUrl)))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth")).components(
            new io.swagger.v3.oas.models.Components().addSecuritySchemes("bearerAuth",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                    .bearerFormat("JWT")));
  }
}
