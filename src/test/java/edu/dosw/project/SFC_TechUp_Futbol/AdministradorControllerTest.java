package edu.dosw.project.SFC_TechUp_Futbol;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.project.SFC_TechUp_Futbol.controller.AdministradorController;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ErrorHandler;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdministradorControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        AdministradorService administradorService = new AdministradorService(
                new AdministradorRepositoryImpl(),
                new OrganizadorRepositoryImpl(),
                new ArbitroRepositoryImpl(),
                new UsuarioRegistradoRepositoryImpl()
        );
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdministradorController(administradorService, new AdministradorValidator()))
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    void registrarOrganizador_conAdministradorValido_retorna200() throws Exception {
        mockMvc.perform(post("/api/administradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(crearBody("Admin", "admin@escuelaing.edu.co"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("ADMINISTRADOR"));

        mockMvc.perform(post("/api/administradores/organizadores")
                        .header("X-Administrador-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(crearBody("Organizador", "organizador@escuelaing.edu.co"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("ORGANIZADOR"))
                .andExpect(jsonPath("$.registradoPor").value(1));
    }

    @Test
    void registrarArbitro_sinAdministradorAutorizado_retorna403() throws Exception {
        mockMvc.perform(post("/api/administradores/arbitros")
                        .header("X-Administrador-Id", 99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(crearBody("Arbitro", "arbitro@escuelaing.edu.co"))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value(403));
    }

    private Map<String, Object> crearBody(String nombre, String email) {
        return Map.of(
                "nombre", nombre,
                "email", email,
                "password", "password123",
                "tipoUsuario", "ESTUDIANTE"
        );
    }
}
