package edu.dosw.project.SFC_TechUp_Futbol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerTest {

    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private JugadorService jugadorService;
    private CapitanService capitanService;
    private ArbitroService arbitroService;
    private OrganizadorService organizadorService;
    private PagoServiceImpl pagoService;
    private PartidoServiceImpl partidoService;
    private EquipoService equipoService;
    private TorneoService torneoService;
    private PerfilDeportivoService perfilService;

    @BeforeEach
    void setUp() {
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        ArbitroMapper arbitroMapper = TestMappers.arbitroMapper(partidoMapper);
        PagoMapper pagoMapper = TestMappers.pagoMapper(equipoMapper);
        PerfilDeportivoMapper perfilMapper = TestMappers.perfilDeportivoMapper();

        Map<String, TorneoEntity> torneoStore = new HashMap<>();
        TorneoJpaRepository torneoRepo = MockRepoHelper.torneoRepo(torneoStore);
        torneoService = new TorneoService(torneoRepo, torneoMapper);

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = MockRepoHelper.equipoRepo(equipoStore);
        equipoService = new EquipoService(equipoRepo, equipoMapper);

        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        JugadorJpaRepository jugadorRepo = MockRepoHelper.jugadorRepo(jugadorStore);
        jugadorService = new JugadorService(jugadorRepo, jugadorMapper);

        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        PartidoJpaRepository partidoRepo = MockRepoHelper.partidoRepo(partidoStore);
        partidoService = new PartidoServiceImpl(partidoRepo, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepo, jugadorMapper);

        Map<String, ArbitroEntity> arbitroStore = new HashMap<>();
        ArbitroJpaRepository arbitroRepo = MockRepoHelper.arbitroRepo(arbitroStore);
        arbitroService = new ArbitroService(arbitroRepo, arbitroMapper);

        Map<String, CapitanEntity> capitanStore = new HashMap<>();
        CapitanJpaRepository capitanRepo = MockRepoHelper.capitanRepo(capitanStore);
        CapitanMapper capitanMapper = TestMappers.capitanMapper(equipoRepo, equipoMapper);
        capitanService = new CapitanService(capitanRepo, capitanMapper, jugadorService, jugadorRepo, jugadorMapper);

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        OrganizadorMapper orgMapper = TestMappers.organizadorMapper(torneoRepo, torneoMapper);
        OrganizadorJpaRepository orgRepo = MockRepoHelper.orgRepo(orgStore);
        organizadorService = new OrganizadorService(orgRepo, orgMapper, torneoService);

        Map<String, PagoEntity> pagoStore = new HashMap<>();
        PagoJpaRepository pagoRepo = MockRepoHelper.pagoRepo(pagoStore);
        pagoService = new PagoServiceImpl(pagoRepo, pagoMapper, equipoRepo, equipoMapper);

        Map<String, PerfilDeportivoEntity> perfilStore = new HashMap<>();
        PerfilDeportivoJpaRepository perfilRepo = MockRepoHelper.perfilRepo(perfilStore);
        perfilService = new PerfilDeportivoServiceImpl(perfilRepo, perfilMapper, jugadorRepo);

        mvc = MockMvcBuilders
                .standaloneSetup(new UsuarioController(jugadorService, capitanService, arbitroService,
                        organizadorService, pagoService, partidoService, new PartidoValidator(),
                        equipoService, torneoService, perfilService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private String crearJugador(String email) throws Exception {
        Map<String, Object> body = Map.of("nombre", "Jugador", "email", email, "password", "pass",
                "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 10, "posicion", "DELANTERO");
        return mapper.readTree(mvc.perform(post("/api/users/players")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString()).path("mensaje").asText();
    }

    private String crearCapitan(String email) throws Exception {
        Map<String, Object> body = Map.of("nombre", "Capitan", "email", email, "password", "pass",
                "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 1, "posicion", "PORTERO");
        return mapper.readTree(mvc.perform(post("/api/users/captains")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString()).get("id").asText();
    }

    private String crearArbitro(String email) throws Exception {
        Map<String, Object> body = Map.of("nombre", "Arbitro", "email", email, "password", "pass",
                "tipoUsuario", "ESTUDIANTE");
        mvc.perform(post("/api/users/referees").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(status().isOk());
        return arbitroService.getArbitros().stream()
                .filter(a -> a.getEmail() != null)
                .reduce((first, second) -> second).get().getId();
    }

    private String crearOrganizador(String email) throws Exception {
        Map<String, Object> body = Map.of("nombre", "Org", "email", email, "password", "pass",
                "tipoUsuario", "ESTUDIANTE");
        return mapper.readTree(mvc.perform(post("/api/users/organizers")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString()).get("id").asText();
    }

    // ── Players ──────────────────────────────────────────────────────────────

    @Test
    void crearJugador_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Juan", "email", "juan@test.com", "password", "pass",
                "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 9, "posicion", "DELANTERO");
        mvc.perform(post("/api/users/players").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(status().isOk());
    }

    @Test
    void listarJugadores_retorna200() throws Exception {
        mvc.perform(get("/api/users/players")).andExpect(status().isOk());
    }

    @Test
    void accionesPorActor_valido_retorna200() throws Exception {
        mvc.perform(get("/api/users/player")).andExpect(status().isOk())
                .andExpect(jsonPath("$.actor").value("player"));
    }

    @Test
    void accionesPorActor_invalido_retorna200ConError() throws Exception {
        mvc.perform(get("/api/users/invalido")).andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Actor not valid"));
    }

    // ── Captains ─────────────────────────────────────────────────────────────

    @Test
    void crearCapitan_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Cap", "email", "cap@test.com", "password", "pass",
                "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 1, "posicion", "PORTERO");
        mvc.perform(post("/api/users/captains").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(status().isOk());
    }

    @Test
    void listarCapitanes_retorna200() throws Exception {
        mvc.perform(get("/api/users/captains")).andExpect(status().isOk());
    }

    @Test
    void invitarJugador_retorna200() throws Exception {
        String capId = crearCapitan("capinv@test.com");
        String jugId = crearJugadorYObtenerId("juginv@test.com", 7, "DEFENSA");
        mvc.perform(post("/api/users/captains/" + capId + "/invite/" + jugId))
                .andExpect(status().isOk());
    }

    private String crearJugadorYObtenerId(String email, int camiseta, String posicion) {
        Jugador jugador = new Jugador(null, "Jug", email, "pass",
                Usuario.TipoUsuario.ESTUDIANTE, camiseta, Jugador.Posicion.valueOf(posicion), true, "");
        return jugadorService.save(jugador).getId();
    }

    @Test
    void aceptarInvitacion_retorna200() throws Exception {
        String jugId = crearJugadorYObtenerId("jugacep@test.com", 5, "VOLANTE");
        mvc.perform(patch("/api/users/players/" + jugId + "/accept-invitation"))
                .andExpect(status().isOk());
    }

    @Test
    void rechazarInvitacion_retorna200() throws Exception {
        String jugId = crearJugadorYObtenerId("jugrech@test.com", 6, "DEFENSA");
        mvc.perform(patch("/api/users/players/" + jugId + "/reject-invitation"))
                .andExpect(status().isOk());
    }

    @Test
    void marcarDisponible_retorna200() throws Exception {
        crearJugadorYObtenerId("jugdisp@test.com", 8, "PORTERO");
        mvc.perform(patch("/api/users/players/jugdisp@test.com/availability"))
                .andExpect(status().isOk());
    }

    @Test
    void toggleRol_jugadorACapitan_retorna200() throws Exception {
        crearJugadorYObtenerId("jugtoggle@test.com", 11, "DELANTERO");
        mvc.perform(patch("/api/users/players/jugtoggle@test.com/profile/toggle-role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Rol cambiado a CAPITAN"));
    }

    @Test
    void toggleRol_capitanAJugador_retorna200() throws Exception {
        crearCapitan("captoggle@test.com");
        mvc.perform(patch("/api/users/players/captoggle@test.com/profile/toggle-role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Rol cambiado a JUGADOR"));
    }

    @Test
    void toggleRol_idInexistente_retorna400() throws Exception {
        mvc.perform(patch("/api/users/players/id-inexistente/profile/toggle-role"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarJugadoresPorPosicion_retorna200() throws Exception {
        String capId = crearCapitan("capbusq@test.com");
        mvc.perform(get("/api/users/captains/" + capId + "/search-players").param("posicion", "PORTERO"))
                .andExpect(status().isOk());
    }

    @Test
    void subirComprobantePago_capitan_retorna200() throws Exception {
        String capId = crearCapitan("capcomp@test.com");
        mvc.perform(post("/api/users/captains/" + capId + "/receipt").param("comprobante", "comp123"))
                .andExpect(status().isOk());
    }

    // ── Referees ─────────────────────────────────────────────────────────────

    @Test
    void crearArbitro_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Arb", "email", "arb@test.com", "password", "pass",
                "tipoUsuario", "ESTUDIANTE");
        mvc.perform(post("/api/users/referees").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(status().isOk());
    }

    @Test
    void listarArbitros_retorna200() throws Exception {
        mvc.perform(get("/api/users/referees")).andExpect(status().isOk());
    }

    @Test
    void consultarPartidosArbitro_retorna200() throws Exception {
        String arbId = crearArbitro("arbpart@test.com");
        mvc.perform(get("/api/users/referees/" + arbId + "/matches")).andExpect(status().isOk());
    }

    // ── Organizers ───────────────────────────────────────────────────────────

    @Test
    void crearOrganizador_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Org", "email", "org@test.com", "password", "pass",
                "tipoUsuario", "ESTUDIANTE");
        mvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(status().isOk());
    }

    @Test
    void listarOrganizadores_retorna200() throws Exception {
        mvc.perform(get("/api/users/organizers")).andExpect(status().isOk());
    }

    @Test
    void configurarTorneo_retorna200() throws Exception {
        String orgId = crearOrganizador("orgconf@test.com");
        Torneo t = new Torneo(null, "Copa Config", LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        mvc.perform(post("/api/users/organizers/" + orgId + "/tournament")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(t)));
        Map<String, Object> config = Map.of("reglamento", "Reglas", "canchas", "Cancha A",
                "horarios", "10:00", "sanciones", "Tarjeta roja");
        mvc.perform(patch("/api/users/organizers/" + orgId + "/tournament/configure")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(config)))
                .andExpect(status().isOk());
    }

    @Test
    void crearPartido_retorna200() throws Exception {
        String orgId = crearOrganizador("orgpartido@test.com");
        Torneo t = new Torneo(null, "Copa Partido", LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        String torneoResp = mvc.perform(post("/api/users/organizers/" + orgId + "/tournament")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(t)))
                .andReturn().getResponse().getContentAsString();
        String torneoId = mapper.readTree(torneoResp).get("id").asText();

        Equipo eq1 = equipoService.crear(new Equipo(null, "Equipo A", "", "rojo", "blanco", null), Map.of("nombre", "Equipo A", "colorPrincipal", "rojo"));
        Equipo eq2 = equipoService.crear(new Equipo(null, "Equipo B", "", "azul", "blanco", null), Map.of("nombre", "Equipo B", "colorPrincipal", "azul"));

        Map<String, Object> body = Map.of("torneoId", torneoId, "equipoLocalId", eq1.getId(),
                "equipoVisitanteId", eq2.getId(), "fecha", "2025-09-10T15:00:00", "cancha", "Cancha 1");
        mvc.perform(post("/api/users/organizers/" + orgId + "/matches")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void pagosPendientes_retorna200() throws Exception {
        String orgId = crearOrganizador("orgpagos@test.com");
        mvc.perform(get("/api/users/organizers/" + orgId + "/payments/pending"))
                .andExpect(status().isOk());
    }

    @Test
    void aprobarPago_inexistente_retorna404() throws Exception {
        String orgId = crearOrganizador("orgaprob@test.com");
        mvc.perform(put("/api/users/organizers/" + orgId + "/payments/id-inexistente/approve"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rechazarPago_inexistente_retorna404() throws Exception {
        String orgId = crearOrganizador("orgrechpago@test.com");
        mvc.perform(put("/api/users/organizers/" + orgId + "/payments/id-inexistente/reject"))
                .andExpect(status().isNotFound());
    }
}
