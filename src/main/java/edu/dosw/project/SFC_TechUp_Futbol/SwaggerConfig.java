package edu.dosw.project.SFC_TechUp_Futbol;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("TechCup Futbol API")
                        .description("API para la gestion del torneo de futbol ")
                        .version("1.0.0"));
    }
}