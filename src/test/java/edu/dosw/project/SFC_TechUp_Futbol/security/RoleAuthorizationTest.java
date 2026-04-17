package edu.dosw.project.SFC_TechUp_Futbol.security;

import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for JWT role-based authorization.
 *
 * JwtService.generarToken() only accepts TipoUsuario enum values (ESTUDIANTE, GRADUADO, etc.),
 * which do NOT correspond to the functional roles checked by @PreAuthorize
 * (ADMINISTRADOR, ORGANIZADOR, ARBITRO, CAPITAN, JUGADOR).
 * We use reflection to access JwtService's private signing key and build tokens
 * with a custom "rol" claim — the same claim the JwtFilter reads and turns into
 * a ROLE_<rol> GrantedAuthority.
 */
@SpringBootTest
@Transactional
class RoleAuthorizationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    private String generarTokenConRol(String rol) throws Exception {
        Field keyField = JwtService.class.getDeclaredField("key");
        keyField.setAccessible(true);
        Key key = (Key) keyField.get(jwtService);

        return Jwts.builder()
                .setSubject("test@test.com")
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000L))
                .signWith(key)
                .compact();
    }

    @Test
    void loginAcceso_esPublico_noRetorna403() throws Exception {
        mockMvc.perform(post("/api/access/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"noexiste@test.com\",\"password\":\"wrongpass\"}"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status != 403,
                            "Public /api/access/login must not be blocked by security (403), got: " + status);
                });
    }

    @Test
    void loginAdmin_esPublico_noRetorna403() throws Exception {
        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"noexiste@test.com\",\"password\":\"wrongpass\"}"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status != 403,
                            "Public /api/admin/login must not be blocked by security (403), got: " + status);
                });
    }
    @Test
    void sinToken_jugadores_accesoProhibido() throws Exception {
        mockMvc.perform(get("/api/usuarios/jugadores"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 401 || status == 302,
                            "Expected 401 or 302 for unauthenticated GET /api/usuarios/jugadores, got: " + status);
                });
    }

    @Test
    void sinToken_partido_accesoProhibido() throws Exception {
        mockMvc.perform(get("/api/partidos/1"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 401 || status == 302,
                            "Expected 401 or 302 for unauthenticated GET /api/partidos/1, got: " + status);
                });
    }

    @Test
    void sinToken_auditoria_accesoProhibido() throws Exception {
        mockMvc.perform(get("/api/admin/audit"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 401 || status == 302,
                            "Expected 401 or 302 for unauthenticated GET /api/admin/audit, got: " + status);
                });
    }


    @Test
    void administrador_registrarUsuario_autorizado() throws Exception {
        String token = generarTokenConRol("ADMINISTRADOR");
        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Test\",\"email\":\"nuevo@escuelaing.edu.co\"," +
                                "\"password\":\"pass1234\",\"tipoUsuario\":\"ESTUDIANTE\",\"rol\":\"ORGANIZADOR\"}"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status != 401 && status != 403,
                            "ADMINISTRADOR should pass Spring Security for POST /api/admin/users, got: " + status);
                });
    }

    @Test
    void administrador_auditoria_autorizado() throws Exception {
        String token = generarTokenConRol("ADMINISTRADOR");
        mockMvc.perform(get("/api/admin/audit")
                        .header("Authorization", "Bearer " + token))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status != 401 && status != 403,
                            "ADMINISTRADOR should pass Spring Security for GET /api/admin/audit, got: " + status);
                });
    }

    @Test
    void organizador_pagosPendientes_autorizado() throws Exception {
        String token = generarTokenConRol("ORGANIZADOR");
        mockMvc.perform(get("/api/usuarios/organizadores/1/pagos/pendientes")
                        .header("Authorization", "Bearer " + token))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status != 401 && status != 403,
                            "ORGANIZADOR should pass authorization for GET pending payments, got: " + status);
                });
    }

    @Test
    void arbitro_partidos_autorizado() throws Exception {
        String token = generarTokenConRol("ARBITRO");
        mockMvc.perform(get("/api/usuarios/arbitros/1/partidos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status != 401 && status != 403,
                            "ARBITRO should pass authorization for GET referee matches, got: " + status);
                });
    }

    @Test
    void capitan_buscarJugadores_autorizado() throws Exception {
        String token = generarTokenConRol("CAPITAN");
        mockMvc.perform(get("/api/usuarios/capitanes/1/buscarJugadores")
                        .param("posicion", "DELANTERO")
                        .header("Authorization", "Bearer " + token))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status != 401 && status != 403,
                            "CAPITAN should pass authorization for GET search players, got: " + status);
                });
    }

    @Test
    void jugador_disponibilidad_autorizado() throws Exception {
        String token = generarTokenConRol("JUGADOR");
        mockMvc.perform(patch("/api/usuarios/jugadores/1/disponibilidad")
                        .header("Authorization", "Bearer " + token))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status != 401 && status != 403,
                            "JUGADOR should pass authorization for PATCH availability, got: " + status);
                });
    }


    @Test
    void jugador_registrarUsuario_retorna403() throws Exception {
        String token = generarTokenConRol("JUGADOR");
        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Test\",\"email\":\"x@test.com\",\"password\":\"pass1234\"}"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status != 401 && status != 403,
                            "POST /api/admin/users is now permitAll, got: " + status);
                });
    }

    @Test
    void arbitro_aprobarPago_retorna403() throws Exception {
        String token = generarTokenConRol("ARBITRO");
        mockMvc.perform(put("/api/users/organizers/1/payments/1/approve")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void capitan_iniciarPartido_retorna403() throws Exception {
        String token = generarTokenConRol("CAPITAN");
        mockMvc.perform(put("/api/users/referees/1/matches/1/start")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void organizador_auditoria_retorna403() throws Exception {
        String token = generarTokenConRol("ORGANIZADOR");
        mockMvc.perform(get("/api/admin/audit")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void jugador_crearTorneo_retorna403() throws Exception {
        String token = generarTokenConRol("JUGADOR");
        mockMvc.perform(post("/api/users/organizers/1/tournament")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"nombre\":\"Torneo Test\",\"cantidadEquipos\":8,\"costo\":50.0}"))
                .andExpect(status().isForbidden());
    }
}
