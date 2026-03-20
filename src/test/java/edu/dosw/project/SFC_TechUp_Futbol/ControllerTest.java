package edu.dosw.project.SFC_TechUp_Futbol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.controller.AccesoController;
import edu.dosw.project.SFC_TechUp_Futbol.controller.EquipoController;
import edu.dosw.project.SFC_TechUp_Futbol.controller.TorneoController;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.TorneoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AccesoServiceImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.TorneoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AccesoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.exception.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ControllerTest {

    private MockMvc accesoMvc;
    private MockMvc torneoMvc;
    private MockMvc equipoMvc;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        AccesoServiceImpl accesoService = new AccesoServiceImpl(new UsuarioRegistradoRepositoryImpl());
        accesoMvc = MockMvcBuilders
                .standaloneSetup(new AccesoController(accesoService, new AccesoValidator()))
                .setControllerAdvice(new ErrorHandler())
                .build();

        TorneoService torneoService = new TorneoService(new TorneoRepositoryImpl());
        torneoMvc = MockMvcBuilders
                .standaloneSetup(new TorneoController(torneoService))
                .setControllerAdvice(new ErrorHandler())
                .build();

        EquipoService equipoService = new EquipoService(new EquipoRepositoryImpl());
        equipoMvc = MockMvcBuilders
                .standaloneSetup(new EquipoController(equipoService))
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    // ── AccesoController ───────────────────────────────────────────────────────

    @Test
    void registro_valido_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Ana",
                "email", "ana@escuelaing.edu.co",
                "password", "12345678",
                "tipoUsuario", "ESTUDIANTE"
        );
        accesoMvc.perform(post("/api/acceso/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@escuelaing.edu.co"));
    }

    @Test
    void registro_correoInvalido_retorna400() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Ana",
                "email", "correo-invalido",
                "password", "12345678",
                "tipoUsuario", "ESTUDIANTE"
        );
        accesoMvc.perform(post("/api/acceso/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_credencialesCorrectas_retornaToken() throws Exception {
        // primero registrar
        Map<String, Object> reg = Map.of(
                "nombre", "Pedro",
                "email", "pedro@escuelaing.edu.co",
                "password", "12345678",
                "tipoUsuario", "ESTUDIANTE"
        );
        accesoMvc.perform(post("/api/acceso/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reg)));

        Map<String, String> login = Map.of("email", "pedro@escuelaing.edu.co", "password", "12345678");
        accesoMvc.perform(post("/api/acceso/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_passwordIncorrecta_retorna400() throws Exception {
        Map<String, Object> reg = Map.of(
                "nombre", "Luis",
                "email", "luis@escuelaing.edu.co",
                "password", "12345678",
                "tipoUsuario", "ESTUDIANTE"
        );
        accesoMvc.perform(post("/api/acceso/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reg)));

        Map<String, String> login = Map.of("email", "luis@escuelaing.edu.co", "password", "wrongpass");
        accesoMvc.perform(post("/api/acceso/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest());
    }

    // ── TorneoController ───────────────────────────────────────────────────────

    @Test
    void crearTorneo_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Copa Test",
                "fechaInicio", "2025-09-01T10:00:00",
                "fechaFin", "2025-09-10T18:00:00",
                "cantidadEquipos", 8,
                "costo", 50.0
        );
        torneoMvc.perform(post("/api/torneos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Copa Test"));
    }

    @Test
    void listarTorneos_retorna200() throws Exception {
        torneoMvc.perform(get("/api/torneos"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerTorneo_inexistente_retorna400() throws Exception {
        torneoMvc.perform(get("/api/torneos/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void iniciarTorneo_existente_retorna200() throws Exception {
        // crear primero
        Map<String, Object> body = Map.of(
                "nombre", "Copa Inicio",
                "fechaInicio", "2025-09-01T10:00:00",
                "fechaFin", "2025-09-10T18:00:00",
                "cantidadEquipos", 8,
                "costo", 50.0
        );
        String resp = torneoMvc.perform(post("/api/torneos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();

        int id = mapper.readTree(resp).get("id").asInt();
        torneoMvc.perform(put("/api/torneos/" + id + "/iniciar"))
                .andExpect(status().isOk());
    }

    // ── EquipoController ───────────────────────────────────────────────────────

    @Test
    void crearEquipo_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Los Tigres",
                "escudo", "tigres.png",
                "colorPrincipal", "rojo",
                "colorSecundario", "blanco",
                "capitanId", 1
        );
        equipoMvc.perform(post("/api/equipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Los Tigres"));
    }

    @Test
    void listarEquipos_retorna200() throws Exception {
        equipoMvc.perform(get("/api/equipos"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerEquipo_inexistente_retorna400() throws Exception {
        equipoMvc.perform(get("/api/equipos/999"))
                .andExpect(status().isBadRequest());
    }
}
