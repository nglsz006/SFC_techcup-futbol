package edu.dosw.project.SFC_TechUp_Futbol.config;

import edu.dosw.project.SFC_TechUp_Futbol.config.SwaggerConfig;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    private final SwaggerConfig config = new SwaggerConfig();

    @Test
    void apiInfo_retornaOpenAPINoNulo() {
        OpenAPI api = config.apiInfo();
        assertNotNull(api);
    }

    @Test
    void apiInfo_tituloEsCorrecto() {
        OpenAPI api = config.apiInfo();
        assertEquals("TechCup Futbol API", api.getInfo().getTitle());
    }

    @Test
    void apiInfo_contieneEsquemaBearer() {
        OpenAPI api = config.apiInfo();
        assertTrue(api.getComponents().getSecuritySchemes().containsKey("Bearer"));
    }
}
