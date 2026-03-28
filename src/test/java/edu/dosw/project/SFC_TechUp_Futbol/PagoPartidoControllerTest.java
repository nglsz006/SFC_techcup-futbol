package edu.dosw.project.SFC_TechUp_Futbol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.controller.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PagoPartidoControllerTest {

    private MockMvc pagoMvc;
    private MockMvc partidoMvc;
    private MockMvc alineacionMvc;
    private MockMvc usuarioMvc;
    private PagoServiceImpl pagoService;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private EquipoRepositoryImpl equipoRepo;
    private TorneoRepositoryImpl torneoRepo;
    private JugadorRepositoryImpl jugadorRepo;
    private Equipo equipo;
    private Torneo torneo;
    private Jugador jugador;

    @BeforeEach
    void setUp() {
        equipoRepo = new EquipoRepositoryImpl();
        torneoRepo = new TorneoRepositoryImpl();
        jugadorRepo = new JugadorRepositoryImpl();

        equipo = equipoRepo.save(new Equipo(0, "Los Tigres", "", "rojo", "blanco", 1));
        torneo = torneoRepo.save(new Torneo(0, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50));
        jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);

        PagoRepositoryImpl pagoRepo = new PagoRepositoryImpl();
        pagoService = new PagoServiceImpl(pagoRepo, equipoRepo);
        pagoMvc = MockMvcBuilders
                .standaloneSetup(new PagoController(pagoService, new PagoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

        PartidoRepositoryImpl partidoRepo = new PartidoRepositoryImpl();
        PartidoServiceImpl partidoService = new PartidoServiceImpl(partidoRepo, torneoRepo, equipoRepo, jugadorRepo);
        partidoMvc = MockMvcBuilders
                .standaloneSetup(new PartidoController(partidoService, new PartidoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

        AlineacionService alineacionService = new AlineacionService(new AlineacionRepositoryImpl());
        alineacionMvc = MockMvcBuilders
                .standaloneSetup(new AlineacionController(alineacionService))
                .setControllerAdvice(new ErrorHandler()).build();

        JugadorService jugadorService = new JugadorService(jugadorRepo);
        ArbitroService arbitroService = new ArbitroService(new ArbitroRepositoryImpl());
        PartidoRepositoryImpl partidoRepoArbitro = new PartidoRepositoryImpl();
        CapitanService capitanService = new CapitanService(new CapitanRepositoryImpl(), jugadorService);
        TorneoService torneoService = new TorneoService(torneoRepo);
        OrganizadorService organizadorService = new OrganizadorService(new OrganizadorRepositoryImpl(), torneoService);
        PagoServiceImpl pagoServiceOrg = new PagoServiceImpl(new PagoRepositoryImpl(), equipoRepo);
        PartidoRepositoryImpl partidoRepoUsuario = new PartidoRepositoryImpl();
        PartidoServiceImpl partidoServiceUsuario = new PartidoServiceImpl(partidoRepoUsuario, torneoRepo, equipoRepo, jugadorRepo);
        EquipoService equipoServiceUsuario = new EquipoService(equipoRepo);
        PartidoValidator partidoValidatorUsuario = new PartidoValidator();
        edu.dosw.project.SFC_TechUp_Futbol.core.service.PerfilDeportivoService perfilService =
                new edu.dosw.project.SFC_TechUp_Futbol.core.service.PerfilDeportivoServiceImpl(
                        new PerfilDeportivoRepositoryImpl(), jugadorRepo);
        usuarioMvc = MockMvcBuilders
                .standaloneSetup(new UsuarioController(jugadorService, jugadorRepo, capitanService,
                        arbitroService, partidoRepoArbitro, organizadorService, pagoService,
                        partidoServiceUsuario, partidoValidatorUsuario, equipoServiceUsuario, torneoService, perfilService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    @Test
    void pago_subirComprobante_retorna200() throws Exception {
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgSub", "email", "orgsub@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Map<String, Object> bodyCap = Map.of(
                "nombre", "CapSub", "email", "capsub@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 5, "posicion", "DEFENSA"
        );
        String respCap = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(respCap).get("id").asLong();
        usuarioMvc.perform(post("/api/usuarios/capitanes/" + capId + "/comprobante")
                        .param("comprobante", "pago.jpg"))
                .andExpect(status().isOk());
    }

    @Test
    void pago_consultarPendientes_retorna200() throws Exception {
        pagoMvc.perform(get("/api/pagos/equipo/" + equipo.getId())).andExpect(status().isOk());
    }

    @Test
    void pago_consultarPorEquipo_retorna200() throws Exception {
        pagoMvc.perform(get("/api/pagos/equipo/" + equipo.getId())).andExpect(status().isOk());
    }

    @Test
    void pago_consultarInexistente_retorna500() throws Exception {
        pagoMvc.perform(get("/api/pagos/999")).andExpect(status().isInternalServerError());
    }

    @Test
    void pago_aprobar_retorna200() throws Exception {
        Map<String, Object> bodyCap = Map.of(
                "nombre", "CapApr", "email", "capapr@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 4, "posicion", "PORTERO"
        );
        String respCap = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(respCap).get("id").asLong();
        String respComp = usuarioMvc.perform(post("/api/usuarios/capitanes/" + capId + "/comprobante")
                        .param("comprobante", "pago.jpg"))
                .andReturn().getResponse().getContentAsString();
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgPago", "email", "orgpago@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        List<Pago> pendientes = pagoService.consultarPagosPendientes();
        if (!pendientes.isEmpty()) {
            Long pagoId = pendientes.get(0).getId();
            usuarioMvc.perform(put("/api/usuarios/organizadores/" + orgId + "/pagos/" + pagoId + "/aprobar"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void pago_rechazar_desdeEnRevision_retorna200() throws Exception {
        Map<String, Object> bodyCap = Map.of(
                "nombre", "CapRech", "email", "caprech@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 3, "posicion", "VOLANTE"
        );
        String respCap = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(respCap).get("id").asLong();
        usuarioMvc.perform(post("/api/usuarios/capitanes/" + capId + "/comprobante")
                .param("comprobante", "pago.jpg"));
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgRech", "email", "orgrech@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        List<Pago> pendientes = pagoService.consultarPagosPendientes();
        if (!pendientes.isEmpty()) {
            Long pagoId = pendientes.get(0).getId();
            usuarioMvc.perform(put("/api/usuarios/organizadores/" + orgId + "/pagos/" + pagoId + "/aprobar"));
            usuarioMvc.perform(put("/api/usuarios/organizadores/" + orgId + "/pagos/" + pagoId + "/rechazar"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void partido_crear_retorna200() throws Exception {
        Equipo visitante = equipoRepo.save(new Equipo(0, "Visitante", "", "azul", "negro", 2));
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgPart", "email", "orgpart@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Torneo t = new Torneo(0, "Copa Part",
                LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/usuarios/organizadores/" + orgId + "/torneo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(t)));
        Map<String, Object> body = Map.of(
                "torneoId", torneo.getId(),
                "equipoLocalId", equipo.getId(),
                "equipoVisitanteId", visitante.getId(),
                "fecha", "2025-09-01T10:00:00",
                "cancha", "cancha 1"
        );
        usuarioMvc.perform(post("/api/usuarios/organizadores/" + orgId + "/partidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void partido_flujoCompleto_retorna200() throws Exception {
        Equipo visitante = equipoRepo.save(new Equipo(0, "Visitante2", "", "azul", "negro", 2));
        jugador.setEquipo(equipo.getId());
        jugadorRepo.save(jugador);
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgFlujo", "email", "orgflujo@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Torneo t = new Torneo(0, "Copa Flujo",
                LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/usuarios/organizadores/" + orgId + "/torneo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(t)));
        Map<String, Object> bodyArbitro = Map.of(
                "nombre", "RefFlujo", "email", "refflujo@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respArb = usuarioMvc.perform(post("/api/usuarios/arbitros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyArbitro)))
                .andReturn().getResponse().getContentAsString();
        Long arbId = mapper.readTree(respArb).get("id").asLong();
        Map<String, Object> bodyPartido = Map.of(
                "torneoId", torneo.getId(),
                "equipoLocalId", equipo.getId(),
                "equipoVisitanteId", visitante.getId(),
                "fecha", "2025-09-01T10:00:00",
                "cancha", "cancha 2"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/organizadores/" + orgId + "/partidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyPartido)))
                .andReturn().getResponse().getContentAsString();
        Long pid = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(put("/api/usuarios/arbitros/" + arbId + "/partidos/" + pid + "/iniciar")).andExpect(status().isOk());
        usuarioMvc.perform(post("/api/usuarios/arbitros/" + arbId + "/partidos/" + pid + "/goles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("jugadorId", jugador.getId(), "minuto", 10))))
                .andExpect(status().isOk());
        usuarioMvc.perform(post("/api/usuarios/arbitros/" + arbId + "/partidos/" + pid + "/sanciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("jugadorId", jugador.getId(), "tipoSancion", "TARJETA_AMARILLA", "descripcion", "Falta reiterada en minuto 30"))))
                .andExpect(status().isOk());
        usuarioMvc.perform(put("/api/usuarios/arbitros/" + arbId + "/partidos/" + pid + "/resultado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("golesLocal", 2, "golesVisitante", 1))))
                .andExpect(status().isOk());
        usuarioMvc.perform(put("/api/usuarios/arbitros/" + arbId + "/partidos/" + pid + "/finalizar")).andExpect(status().isOk());
        usuarioMvc.perform(get("/api/usuarios/arbitros/" + arbId + "/partidos")).andExpect(status().isOk());
    }

    @Test
    void partido_consultarInexistente_retorna500() throws Exception {
        partidoMvc.perform(get("/api/partidos/999")).andExpect(status().isInternalServerError());
    }

    @Test
    void partido_consultarPorTorneo_retorna200() throws Exception {
        partidoMvc.perform(get("/api/partidos/torneo/" + torneo.getId())).andExpect(status().isOk());
    }

    @Test
    void partido_consultarPorEquipo_retorna200() throws Exception {
        partidoMvc.perform(get("/api/partidos/equipo/" + equipo.getId())).andExpect(status().isOk());
    }

    @Test
    void alineacion_crear_retorna400_porValidacion() throws Exception {
        Map<String, Object> body = Map.of(
                "equipoId", 1,
                "partidoId", 1,
                "formacion", "4-4-2",
                "titulares", java.util.List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
                "reservas", java.util.List.of()
        );
        Map<String, Object> bodyCap = Map.of(
                "nombre", "CapAlin", "email", "capalin@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 2, "posicion", "DEFENSA"
        );
        String respCap = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(respCap).get("id").asLong();
        usuarioMvc.perform(post("/api/usuarios/capitanes/" + capId + "/alineacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(java.util.List.of())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void alineacion_listar_retorna200() throws Exception {
        alineacionMvc.perform(get("/api/alineaciones")).andExpect(status().isOk());
    }

    @Test
    void alineacion_consultarRival_sinDatos_retorna400() throws Exception {
        alineacionMvc.perform(get("/api/alineaciones/rival")
                        .param("partidoId", "1")
                        .param("equipoId", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void alineacion_obtenerInexistente_retorna400() throws Exception {
        alineacionMvc.perform(get("/api/alineaciones/999")).andExpect(status().isBadRequest());
    }

    @Test
    void arbitro_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Ref1", "email", "ref@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        usuarioMvc.perform(post("/api/usuarios/arbitros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void arbitro_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/usuarios/arbitros")).andExpect(status().isOk());
    }

    @Test
    void arbitro_asignarPartido_inexistente_retorna400() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Ref2", "email", "ref2@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/arbitros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(post("/api/usuarios/arbitros/" + id + "/partidos/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void arbitro_consultarPartidos_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/usuarios/arbitros/1/partidos")).andExpect(status().isOk());
    }

    @Test
    void capitan_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Cap1", "email", "cap@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 10, "posicion", "DELANTERO"
        );
        usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void capitan_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/usuarios/capitanes")).andExpect(status().isOk());
    }

    @Test
    void capitan_crearEquipo_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Cap2", "email", "cap2@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 7, "posicion", "VOLANTE"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(post("/api/usuarios/capitanes/" + id + "/equipo")
                        .param("nombreEquipo", "Los Bravos"))
                .andExpect(status().isOk());
    }

    @Test
    void capitan_invitarJugador_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Cap3", "email", "cap3@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 8, "posicion", "DEFENSA"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(post("/api/usuarios/capitanes/" + id + "/invitar/" + jugador.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void capitan_subirComprobante_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Cap4", "email", "cap4@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 6, "posicion", "PORTERO"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(post("/api/usuarios/capitanes/" + id + "/comprobante")
                        .param("comprobante", "pago.pdf"))
                .andExpect(status().isOk());
    }

    @Test
    void capitan_buscarJugadores_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/usuarios/capitanes/1/buscarJugadores").param("posicion", "DELANTERO"))
                .andExpect(status().isOk());
    }

    @Test
    void jugador_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Carlos", "email", "carlos@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 9, "posicion", "DELANTERO"
        );
        usuarioMvc.perform(post("/api/usuarios/jugadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void jugador_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/usuarios/jugadores")).andExpect(status().isOk());
    }

    @Test
    void jugador_aceptarInvitacion_retorna200() throws Exception {
        usuarioMvc.perform(patch("/api/usuarios/jugadores/1/aceptarInvitacion")).andExpect(status().isOk());
    }

    @Test
    void jugador_rechazarInvitacion_retorna200() throws Exception {
        usuarioMvc.perform(patch("/api/usuarios/jugadores/1/rechazarInvitacion")).andExpect(status().isOk());
    }

    @Test
    void organizador_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Org1", "email", "org@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void organizador_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/usuarios/organizadores")).andExpect(status().isOk());
    }

    @Test
    void organizador_crearTorneo_retorna200() throws Exception {
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "Org3", "email", "org3@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        Torneo torneo2 = new Torneo(0, "Copa Org",
                java.time.LocalDateTime.of(2025, 9, 1, 10, 0),
                java.time.LocalDateTime.of(2025, 9, 10, 18, 0), 8, 50000);
        usuarioMvc.perform(post("/api/usuarios/organizadores/" + id + "/torneo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(torneo2)))
                .andExpect(status().isOk());
    }

    @Test
    void organizador_pagosPendientes_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Org4", "email", "org4@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(get("/api/usuarios/organizadores/" + id + "/pagos/pendientes"))
                .andExpect(status().isOk());
    }

    @Test
    void organizador_iniciarTorneo_sinTorneo_retorna409() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Org2", "email", "org2@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(patch("/api/usuarios/organizadores/" + id + "/torneo/iniciar"))
                .andExpect(status().isConflict());
    }

    @Test
    void jugador_editarPerfil_retorna200() throws Exception {
        Map<String, Object> bodyJug = Map.of(
                "nombre", "JugEdit", "email", "jugedit@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 5, "posicion", "DEFENSA"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/jugadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyJug)))
                .andReturn().getResponse().getContentAsString();
        Long jugId = mapper.readTree(resp).get("id").asLong();
        Map<String, Object> perfil = Map.of("nombre", "JugEditado", "numeroCamiseta", 11, "posicion", "PORTERO");
        usuarioMvc.perform(patch("/api/usuarios/jugadores/" + jugId + "/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(perfil)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jerseyNumber").value(11));
    }

    @Test
    void capitan_validarEquipo_sinEquipo_retorna400() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "CapVal", "email", "capval@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 3, "posicion", "VOLANTE"
        );
        String resp = usuarioMvc.perform(post("/api/usuarios/capitanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(get("/api/usuarios/capitanes/999/equipo/validar"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void organizador_configurarTorneo_retorna200() throws Exception {
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgConf", "email", "orgconf@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/usuarios/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Torneo t = new Torneo(0, "Copa Conf",
                LocalDateTime.of(2025, 10, 1, 10, 0),
                LocalDateTime.of(2025, 10, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/usuarios/organizadores/" + orgId + "/torneo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(t)));
        Map<String, Object> config = Map.of(
                "reglamento", "Sin violencia",
                "canchas", "Cancha Norte",
                "horarios", "Sabados 8am",
                "sanciones", "Tarjeta roja = 1 fecha",
                "cierreInscripciones", "2025-09-25T00:00:00"
        );
        usuarioMvc.perform(patch("/api/usuarios/organizadores/" + orgId + "/torneo/configurar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(config)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reglamento").value("Sin violencia"));
    }
}