package com.github.ssuyoi.projectvoucher.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // http://localhost:8080/swagger-ui/index.html
    @Bean
    public OpenAPI openApiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("Project Voucher API")
                .description("Project Voucher API Documentation")
                .version(LocalDateTime.now().toString()));
    }
}
