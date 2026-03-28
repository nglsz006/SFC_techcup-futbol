package edu.dosw.project.SFC_TechUp_Futbol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.controller.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AccesoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ControllerTest {

    private MockMvc accesoMvc;
    private MockMvc torneoMvc;
    private MockMvc equipoMvc;
    private MockMvc partidoMvc;
    private MockMvc usuarioMvc;
    private TorneoRepositoryImpl torneoRepo2;
    private EquipoRepositoryImpl equipoRepo2;
    private JugadorRepositoryImpl jugadorRepo2;
    private PartidoRepositoryImpl partidoRepo2;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        AccesoServiceImpl accesoService = new AccesoServiceImpl(new UsuarioRegistradoRepositoryImpl(), new edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService());
        accesoMvc = MockMvcBuilders
                .standaloneSetup(new AccesoController(accesoService, new AccesoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

        torneoRepo2 = new TorneoRepositoryImpl();
        equipoRepo2 = new EquipoRepositoryImpl();
        jugadorRepo2 = new JugadorRepositoryImpl();
        partidoRepo2 = new PartidoRepositoryImpl();
        TorneoService torneoService = new TorneoService(torneoRepo2);
        PartidoServiceImpl partidoService2 = new PartidoServiceImpl(partidoRepo2, torneoRepo2, equipoRepo2, jugadorRepo2);
        EquipoService equipoService2 = new EquipoService(equipoRepo2);

        torneoMvc = MockMvcBuilders
                .standaloneSetup(new TorneoController(torneoService, partidoService2, equipoService2))
                .setControllerAdvice(new ErrorHandler()).build();

        partidoMvc = MockMvcBuilders
                .standaloneSetup(new PartidoController(partidoService2, new PartidoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

        equipoMvc = MockMvcBuilders
                .standaloneSetup(new EquipoController(equipoService2))
                .setControllerAdvice(new ErrorHandler()).build();

        JugadorService jugadorService = new JugadorService(jugadorRepo2);
        ArbitroService arbitroService = new ArbitroService(new ArbitroRepositoryImpl());
        CapitanService capitanService = new CapitanService(new CapitanRepositoryImpl(), jugadorService);
        OrganizadorService organizadorService = new OrganizadorService(new OrganizadorRepositoryImpl(), torneoService);
        PagoServiceImpl pagoService = new PagoServiceImpl(new PagoRepositoryImpl(), equipoRepo2);
        PartidoValidator partidoValidator = new PartidoValidator();
        edu.dosw.project.SFC_TechUp_Futbol.core.service.PerfilDeportivoService perfilService =
                new edu.dosw.project.SFC_TechUp_Futbol.core.service.PerfilDeportivoServiceImpl(
                        new PerfilDeportivoRepositoryImpl(), jugadorRepo2);
        usuarioMvc = MockMvcBuilders
                .standaloneSetup(new UsuarioController(jugadorService, jugadorRepo2, capitanService,
                        arbitroService, partidoRepo2, organizadorService, pagoService,
                        partidoService2, partidoValidator, equipoService2, torneoService, perfilService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    @Test
    void registro_valido_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Ana", "email", "ana@escuelaing.edu.co",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE"
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
                "nombre", "Ana", "email", "correo-invalido",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE"
        );
        accesoMvc.perform(post("/api/acceso/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_credencialesCorrectas_retornaToken() throws Exception {
        Map<String, Object> reg = Map.of(
                "nombre", "Pedro", "email", "pedro@escuelaing.edu.co",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE"
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
                "nombre", "Luis", "email", "luis@escuelaing.edu.co",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE"
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

    private Long crearOrganizadorYTorneo(String emailOrg, String nombreTorneo) throws Exception {
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "Org", "email", emailOrg,
                "password", "12345678", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Torneo t = new Torneo(0, nombreTorneo,
                LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        String respTorneo = usuarioMvc.perform(post("/api/usuarios/organizadores/" + orgId + "/torneo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(t)))
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(respTorneo).get("id").asLong();
    }

    @Test
    void crearTorneo_retorna200() throws Exception {
        Long torneoId = crearOrganizadorYTorneo("orgt@escuelaing.edu.co", "Copa Test");
        torneoMvc.perform(get("/api/torneos/" + torneoId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Copa Test"));
    }

    @Test
    void listarTorneos_retorna200() throws Exception {
        torneoMvc.perform(get("/api/torneos")).andExpect(status().isOk());
    }

    @Test
    void obtenerTorneo_inexistente_retorna400() throws Exception {
        torneoMvc.perform(get("/api/torneos/999")).andExpect(status().isBadRequest());
    }

    @Test
    void iniciarTorneo_existente_retorna200() throws Exception {
        Long torneoId = crearOrganizadorYTorneo("orgi@escuelaing.edu.co", "Copa Inicio");
        Map<String, Object> bodyOrg2 = Map.of(
                "nombre", "Org2", "email", "orgi2@escuelaing.edu.co",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg2 = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg2)))
                .andReturn().getResponse().getContentAsString();
        Long orgId2 = mapper.readTree(respOrg2).get("id").asLong();
        Torneo t2 = new Torneo(0, "Copa Inicio2",
                LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/usuarios/organizadores/" + orgId2 + "/torneo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(t2)));
        usuarioMvc.perform(patch("/api/usuarios/organizadores/" + orgId2 + "/torneo/iniciar"))
                .andExpect(status().isOk());
    }

    @Test
    void finalizarTorneo_existente_retorna200() throws Exception {
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgF", "email", "orgf@escuelaing.edu.co",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Torneo t = new Torneo(0, "Copa Final",
                LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/usuarios/organizadores/" + orgId + "/torneo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(t)));
        usuarioMvc.perform(patch("/api/usuarios/organizadores/" + orgId + "/torneo/iniciar"));
        usuarioMvc.perform(patch("/api/usuarios/organizadores/" + orgId + "/torneo/finalizar"))
                .andExpect(status().isOk());
    }

    @Test
    void tablaPosicionesConPartidosFinalizados_retorna200() throws Exception {
        Torneo torneo = torneoRepo2.save(new Torneo(0, "Copa Cobertura",
                LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50));
        Equipo local = equipoRepo2.save(new Equipo(0, "Local", "", "rojo", "blanco", 1));
        Equipo visitante = equipoRepo2.save(new Equipo(0, "Visitante", "", "azul", "negro", 2));
        Jugador jugador = new Jugador(1L, "Juan", "j@test.com", "pass",
                Usuario.TipoUsuario.ESTUDIANTE, 9, Jugador.Posicion.DELANTERO, true, "");
        jugador.setEquipo(local.getId());
        jugadorRepo2.save(jugador);
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgCob", "email", "orgcob@escuelaing.edu.co",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Map<String, Object> bodyPartido = Map.of(
                "torneoId", (long) torneo.getId(),
                "equipoLocalId", (long) local.getId(),
                "equipoVisitanteId", (long) visitante.getId(),
                "fecha", "2025-09-10T15:00:00",
                "cancha", "Cancha 1"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/organizadores/" + orgId + "/partidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyPartido)))
                .andReturn().getResponse().getContentAsString();
        Long pid = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(put("/api/usuarios/arbitros/1/partidos/" + pid + "/iniciar"));
        usuarioMvc.perform(post("/api/usuarios/arbitros/1/partidos/" + pid + "/goles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("jugadorId", 1L, "minuto", 20))));
        usuarioMvc.perform(put("/api/usuarios/arbitros/1/partidos/" + pid + "/resultado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("golesLocal", 2, "golesVisitante", 1))));
        usuarioMvc.perform(put("/api/usuarios/arbitros/1/partidos/" + pid + "/finalizar"));
        torneoMvc.perform(get("/api/torneos/" + torneo.getId() + "/posiciones")).andExpect(status().isOk());
        torneoMvc.perform(get("/api/torneos/" + torneo.getId() + "/llave")).andExpect(status().isOk());
        torneoMvc.perform(get("/api/torneos/" + torneo.getId() + "/estadisticas")).andExpect(status().isOk());
    }

    @Test
    void tablaPosiciones_retorna200() throws Exception {
        Long torneoId = crearOrganizadorYTorneo("orgpos@escuelaing.edu.co", "Copa Pos");
        torneoMvc.perform(get("/api/torneos/" + torneoId + "/posiciones")).andExpect(status().isOk());
    }

    @Test
    void llaveEliminatoria_retorna200() throws Exception {
        Long torneoId = crearOrganizadorYTorneo("orgllave@escuelaing.edu.co", "Copa Llave");
        torneoMvc.perform(get("/api/torneos/" + torneoId + "/llave")).andExpect(status().isOk());
    }

    @Test
    void estadisticasTorneo_retorna200() throws Exception {
        Long torneoId = crearOrganizadorYTorneo("orgstats@escuelaing.edu.co", "Copa Stats");
        torneoMvc.perform(get("/api/torneos/" + torneoId + "/estadisticas")).andExpect(status().isOk());
    }

    @Test
    void listarEquipos_retorna200() throws Exception {
        equipoMvc.perform(get("/api/equipos")).andExpect(status().isOk());
    }

    @Test
    void obtenerEquipo_existente_retorna200() throws Exception {
        Equipo e = equipoRepo2.save(new Equipo(0, "Los Leones", "", "azul", "blanco", 1));
        equipoMvc.perform(get("/api/equipos/" + e.getId())).andExpect(status().isOk());
    }

    @Test
    void obtenerEquipo_inexistente_retorna400() throws Exception {
        equipoMvc.perform(get("/api/equipos/999")).andExpect(status().isBadRequest());
    }

    @Test
    void agregarJugador_equipo_retorna200() throws Exception {
        Equipo e = equipoRepo2.save(new Equipo(0, "Equipo Jugador", "", "verde", "blanco", 1));
        Jugador j = new Jugador(99L, "Test", "t@test.com", "pass",
                Usuario.TipoUsuario.ESTUDIANTE, 5, Jugador.Posicion.DEFENSA, true, "");
        jugadorRepo2.save(j);
        Map<String, Object> bodyCap = Map.of(
                "nombre", "CapAgregar", "email", "capagregar@escuelaing.edu.co",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 5, "posicion", "DEFENSA"
        );
        String respCap = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(respCap).get("id").asLong();
        usuarioMvc.perform(post("/api/usuarios/capitanes/" + capId + "/invitar/" + j.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void crearEquipo_viaCap_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Cap Test", "email", "captest@escuelaing.edu.co",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 10, "posicion", "DELANTERO"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(post("/api/usuarios/capitanes/" + capId + "/equipo")
                        .param("nombreEquipo", "Los Tigres"))
                .andExpect(status().isOk());
    }
}
