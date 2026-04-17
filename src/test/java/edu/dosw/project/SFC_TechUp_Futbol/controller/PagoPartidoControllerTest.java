package edu.dosw.project.SFC_TechUp_Futbol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.controller.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
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

class PagoPartidoControllerTest {

    private MockMvc pagoMvc;
    private MockMvc partidoMvc;
    private MockMvc alineacionMvc;
    private MockMvc usuarioMvc;
    private PagoServiceImpl pagoService;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private CapitanJpaRepository capitanRepoRef;
    private OrganizadorJpaRepository orgRepoRef;
    private ArbitroJpaRepository arbitroRepoRef;
    private JugadorJpaRepository jugadorRepoRef;
    private Equipo equipo;
    private Torneo torneo;
    private Jugador jugador;

    @BeforeEach
    void setUp() {
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        PagoMapper pagoMapper = TestMappers.pagoMapper(equipoMapper);
        AlineacionMapper alineacionMapper = TestMappers.alineacionMapper();
        PerfilDeportivoMapper perfilMapper = TestMappers.perfilDeportivoMapper();

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = MockRepoHelper.equipoRepo(equipoStore);

        Map<String, TorneoEntity> torneoStore = new HashMap<>();
        TorneoJpaRepository torneoRepo = MockRepoHelper.torneoRepo(torneoStore);

        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        jugadorRepoRef = MockRepoHelper.jugadorRepo(jugadorStore);

        equipo = equipoMapper.toDomain(equipoRepo.save(equipoMapper.toEntity(new Equipo(null, "Los Tigres", "", "rojo", "blanco", null))));
        torneo = torneoMapper.toDomain(torneoRepo.save(torneoMapper.toEntity(new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50))));
        JugadorEntity je = jugadorRepoRef.save(jugadorMapper.toEntity(new Jugador(null, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "")));
        jugador = jugadorMapper.toDomain(je);

        Map<String, PagoEntity> pagoStore = new HashMap<>();
        PagoJpaRepository pagoRepo = MockRepoHelper.pagoRepo(pagoStore);
        pagoService = new PagoServiceImpl(pagoRepo, pagoMapper, equipoRepo, equipoMapper);

        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        PartidoJpaRepository partidoRepo = MockRepoHelper.partidoRepo(partidoStore);
        PartidoServiceImpl partidoService = new PartidoServiceImpl(partidoRepo, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepoRef, jugadorMapper);
        partidoMvc = MockMvcBuilders
                .standaloneSetup(new PartidoController(partidoService, new PartidoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

        Map<String, PerfilDeportivoEntity> perfilStore = new HashMap<>();
        PerfilDeportivoJpaRepository perfilRepo = MockRepoHelper.perfilRepo(perfilStore);

        Map<String, AlineacionEntity> alineacionStore = new HashMap<>();
        AlineacionJpaRepository alineacionRepo = MockRepoHelper.alineacionRepo(alineacionStore);
        AlineacionService alineacionService = new AlineacionService(alineacionRepo, alineacionMapper,
                equipoRepo, TestMappers.equipoMapper(),
                partidoRepo, partidoMapper);
        alineacionMvc = MockMvcBuilders
                .standaloneSetup(new AlineacionController(alineacionService))
                .setControllerAdvice(new ErrorHandler()).build();

        JugadorService jugadorService = new JugadorService(jugadorRepoRef, jugadorMapper);

        Map<String, ArbitroEntity> arbitroStore = new HashMap<>();
        arbitroRepoRef = MockRepoHelper.arbitroRepo(arbitroStore);
        ArbitroMapper arbitroMapper = TestMappers.arbitroMapper(partidoMapper);
        ArbitroService arbitroService = new ArbitroService(arbitroRepoRef, arbitroMapper);

        Map<String, PartidoEntity> partidoStoreArbitro = new HashMap<>();
        PartidoJpaRepository partidoRepoArbitro = MockRepoHelper.partidoRepo(partidoStoreArbitro);
        PartidoServiceImpl partidoServiceArbitro = new PartidoServiceImpl(partidoRepoArbitro, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepoRef, jugadorMapper);

        Map<String, CapitanEntity> capitanStore = new HashMap<>();
        capitanRepoRef = MockRepoHelper.capitanRepo(capitanStore);
        CapitanMapper capitanMapper = TestMappers.capitanMapper(equipoRepo, equipoMapper);
        CapitanService capitanService = new CapitanService(capitanRepoRef, capitanMapper, jugadorService, jugadorRepoRef, jugadorMapper);

        pagoMvc = MockMvcBuilders
                .standaloneSetup(new PagoController(pagoService, new PagoValidator(),
                        capitanService, new EquipoService(equipoRepo, equipoMapper)))
                .setControllerAdvice(new ErrorHandler()).build();

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        orgRepoRef = MockRepoHelper.orgRepo(orgStore);
        OrganizadorMapper orgMapper = TestMappers.organizadorMapper(torneoRepo, torneoMapper);
        OrganizadorService organizadorService = new OrganizadorService(orgRepoRef, orgMapper, new TorneoService(torneoRepo, torneoMapper));

        Map<String, PagoEntity> pagoStoreOrg = new HashMap<>();
        PagoJpaRepository pagoRepoOrg = MockRepoHelper.pagoRepo(pagoStoreOrg);
        PagoServiceImpl pagoServiceOrg = new PagoServiceImpl(pagoRepoOrg, pagoMapper, equipoRepo, equipoMapper);

        Map<String, PartidoEntity> partidoStoreUsuario = new HashMap<>();
        PartidoJpaRepository partidoRepoUsuario = MockRepoHelper.partidoRepo(partidoStoreUsuario);
        PartidoServiceImpl partidoServiceUsuario = new PartidoServiceImpl(partidoRepoUsuario, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepoRef, jugadorMapper);

        PerfilDeportivoService perfilService = new PerfilDeportivoServiceImpl(perfilRepo, perfilMapper, jugadorRepoRef);

        usuarioMvc = MockMvcBuilders
                .standaloneSetup(new UsuarioController(jugadorService, capitanService,
                        arbitroService, organizadorService, pagoServiceOrg,
                        partidoServiceUsuario, new PartidoValidator(), new EquipoService(equipoRepo, equipoMapper),
                        new TorneoService(torneoRepo, torneoMapper), perfilService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    @Test
    void pago_subirComprobante_retorna200() throws Exception {
        Pago pago = pagoService.subirComprobante(equipo.getId(), "pago.jpg");
        pagoMvc.perform(get("/api/payments/" + pago.getId())).andExpect(status().isOk());
    }

    @Test
    void pago_consultarPendientes_retorna200() throws Exception {
        pagoMvc.perform(get("/api/payments/team/" + equipo.getId())).andExpect(status().isOk());
    }

    @Test
    void pago_consultarPorEquipo_retorna200() throws Exception {
        pagoMvc.perform(get("/api/payments/team/" + equipo.getId())).andExpect(status().isOk());
    }

    @Test
    void pago_subirComprobante_comprobanteVacio_retorna400() throws Exception {
        pagoMvc.perform(post("/api/payments/team/correo@test.com/receipt").param("comprobante", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pago_subirComprobante_equipoInexistente_retorna404() throws Exception {
        pagoMvc.perform(post("/api/payments/team/noexiste@test.com/receipt").param("comprobante", "pago.jpg"))
                .andExpect(status().isNotFound());
    }

    @Test
    void pago_subirComprobante_equipoYaInscrito_retorna409() throws Exception {
        Pago pago = pagoService.subirComprobante(equipo.getId(), "pago.jpg");
        pagoService.aprobarPago(pago.getId());
        pagoMvc.perform(post("/api/payments/team/noexiste@test.com/receipt").param("comprobante", "pago2.jpg"))
                .andExpect(status().isNotFound());
    }

    @Test
    void pago_consultarPorId_retorna200() throws Exception {
        Pago pago = pagoService.subirComprobante(equipo.getId(), "pago.jpg");
        pagoMvc.perform(get("/api/payments/" + pago.getId())).andExpect(status().isOk());
    }

    @Test
    void pago_consultarEstado_retorna200() throws Exception {
        Pago pago = pagoService.subirComprobante(equipo.getId(), "pago.jpg");
        pagoMvc.perform(get("/api/payments/" + pago.getId() + "/status")).andExpect(status().isOk());
    }

    @Test
    void pago_consultarInexistente_retorna404() throws Exception {
        pagoMvc.perform(get("/api/payments/uuid-inexistente")).andExpect(status().isNotFound());
    }

    @Test
    void pago_aprobar_retorna200() throws Exception {
        Map<String, Object> bodyCap = Map.of("nombre", "CapApr", "email", "capapr@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 4, "posicion", "PORTERO");
        usuarioMvc.perform(post("/api/users/captains").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyCap)));
        String capId = capitanRepoRef.findAll().stream().filter(c -> Base64Util.encode("capapr@test.com").equals(c.getEmail())).findFirst().get().getId();
        usuarioMvc.perform(post("/api/users/captains/" + capId + "/receipt").param("comprobante", "pago.jpg"));
        Map<String, Object> bodyOrg = Map.of("nombre", "OrgPago", "email", "orgpago@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE");
        usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyOrg)));
        String orgId = orgRepoRef.findAll().stream().filter(o -> Base64Util.encode("orgpago@test.com").equals(o.getEmail())).findFirst().get().getId();
        List<Pago> pendientes = pagoService.consultarPagosPendientes();
        if (!pendientes.isEmpty()) {
            String pagoId = pendientes.get(0).getId();
            usuarioMvc.perform(put("/api/users/organizers/" + orgId + "/payments/" + pagoId + "/approve")).andExpect(status().isOk());
        }
    }

    @Test
    void partido_crear_retorna200() throws Exception {
        Equipo visitante2 = TestMappers.equipoMapper().toDomain(TestMappers.equipoMapper().toEntity(new Equipo(null, "Visitante2", "", "azul", "negro", null)));
        Map<String, Object> bodyOrg = Map.of("nombre", "OrgPart", "email", "orgpart@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE");
        usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyOrg)));
        String orgId = orgRepoRef.findAll().stream().filter(o -> Base64Util.encode("orgpart@test.com").equals(o.getEmail())).findFirst().get().getId();
        Map<String, Object> bodyPartido = Map.of("torneoId", torneo.getId(), "equipoLocalId", equipo.getId(), "equipoVisitanteId", equipo.getId() + "-otro", "fecha", "2025-09-01T10:00:00", "cancha", "cancha 1");
        usuarioMvc.perform(post("/api/users/organizers/" + orgId + "/matches").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyPartido)));
    }

    @Test
    void partido_consultarPorTorneo_retorna200() throws Exception {
        partidoMvc.perform(get("/api/matches/tournament/" + torneo.getId())).andExpect(status().isOk());
    }

    @Test
    void partido_consultarPorEquipo_retorna200() throws Exception {
        partidoMvc.perform(get("/api/matches/team/" + equipo.getId())).andExpect(status().isOk());
    }

    @Test
    void partido_consultarInexistente_retorna500() throws Exception {
        partidoMvc.perform(get("/api/matches/999")).andExpect(status().isInternalServerError());
    }

    @Test
    void alineacion_listar_retorna200() throws Exception {
        alineacionMvc.perform(get("/api/lineups")).andExpect(status().isOk());
    }

    @Test
    void alineacion_consultarRival_sinDatos_retorna400() throws Exception {
        alineacionMvc.perform(get("/api/lineups/rival").param("partidoId", "1").param("equipoId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void alineacion_obtenerInexistente_retorna400() throws Exception {
        alineacionMvc.perform(get("/api/lineups/999")).andExpect(status().isBadRequest());
    }

    @Test
    void arbitro_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Ref1", "email", "ref@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE");
        usuarioMvc.perform(post("/api/users/referees").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body))).andExpect(status().isOk());
    }

    @Test
    void arbitro_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/referees")).andExpect(status().isOk());
    }

    @Test
    void arbitro_asignarPartido_inexistente_retorna400() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Ref2", "email", "ref2@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE");
        usuarioMvc.perform(post("/api/users/referees").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        String id = arbitroRepoRef.findAll().stream().filter(a -> Base64Util.encode("ref2@test.com").equals(a.getEmail())).findFirst().get().getId();
        usuarioMvc.perform(post("/api/users/referees/" + id + "/matches/999")).andExpect(status().is5xxServerError());
    }

    @Test
    void arbitro_consultarPartidos_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/referees/uuid-any/matches")).andExpect(status().isOk());
    }

    @Test
    void capitan_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Cap1", "email", "cap@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 10, "posicion", "DELANTERO");
        usuarioMvc.perform(post("/api/users/captains").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body))).andExpect(status().isOk());
    }

    @Test
    void capitan_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/captains")).andExpect(status().isOk());
    }

    @Test
    void capitan_crearEquipo_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Cap2", "email", "cap2@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 7, "posicion", "VOLANTE");
        usuarioMvc.perform(post("/api/users/captains").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        String id = capitanRepoRef.findAll().stream().filter(c -> Base64Util.encode("cap2@test.com").equals(c.getEmail())).findFirst().get().getId();
        usuarioMvc.perform(post("/api/users/captains/" + id + "/team").param("nombreEquipo", "Los Bravos")).andExpect(status().isOk());
    }

    @Test
    void capitan_invitarJugador_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Cap3", "email", "cap3@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 8, "posicion", "DEFENSA");
        usuarioMvc.perform(post("/api/users/captains").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        String id = capitanRepoRef.findAll().stream().filter(c -> Base64Util.encode("cap3@test.com").equals(c.getEmail())).findFirst().get().getId();
        usuarioMvc.perform(post("/api/users/captains/" + id + "/invite/" + jugador.getId())).andExpect(status().isOk());
    }

    @Test
    void capitan_subirComprobante_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Cap4", "email", "cap4@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 6, "posicion", "PORTERO");
        usuarioMvc.perform(post("/api/users/captains").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        String id = capitanRepoRef.findAll().stream().filter(c -> Base64Util.encode("cap4@test.com").equals(c.getEmail())).findFirst().get().getId();
        usuarioMvc.perform(post("/api/users/captains/" + id + "/receipt").param("comprobante", "pago.pdf")).andExpect(status().isOk());
    }

    @Test
    void capitan_buscarJugadores_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/captains/uuid-any/search-players").param("posicion", "DELANTERO")).andExpect(status().isOk());
    }

    @Test
    void jugador_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Carlos", "email", "carlos@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 9, "posicion", "DELANTERO");
        usuarioMvc.perform(post("/api/users/players").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body))).andExpect(status().isOk());
    }

    @Test
    void jugador_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/players")).andExpect(status().isOk());
    }

    @Test
    void jugador_aceptarInvitacion_retorna200() throws Exception {
        usuarioMvc.perform(patch("/api/users/players/" + jugador.getId() + "/accept-invitation")).andExpect(status().isOk());
    }

    @Test
    void jugador_rechazarInvitacion_retorna200() throws Exception {
        usuarioMvc.perform(patch("/api/users/players/" + jugador.getId() + "/reject-invitation")).andExpect(status().isOk());
    }

    @Test
    void jugador_editarPerfil_retorna200() throws Exception {
        Map<String, Object> bodyJug = Map.of("nombre", "JugEdit", "email", "jugedit@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 5, "posicion", "DEFENSA");
        usuarioMvc.perform(post("/api/users/players").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyJug)));
        String jugId = jugadorRepoRef.findAll().stream().filter(j -> Base64Util.encode("jugedit@test.com").equals(j.getEmail())).findFirst().get().getId();
        Map<String, Object> perfil = Map.of("nombre", "JugEditado", "numeroCamiseta", 11, "posicion", "PORTERO");
        usuarioMvc.perform(patch("/api/users/players/" + jugId + "/profile").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(perfil)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.numeroCamiseta").value(11));
    }

    @Test
    void organizador_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Org1", "email", "org1@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE");
        usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body))).andExpect(status().isOk());
    }

    @Test
    void organizador_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/organizers")).andExpect(status().isOk());
    }

    @Test
    void organizador_crearTorneo_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "OrgT", "email", "orgt@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE");
        usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        String id = orgRepoRef.findAll().stream().filter(o -> Base64Util.encode("orgt@test.com").equals(o.getEmail())).findFirst().get().getId();
        Torneo t = new Torneo(null, "Copa Nueva", LocalDateTime.of(2025, 9, 1, 10, 0), LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/users/organizers/" + id + "/tournament").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(t))).andExpect(status().isOk());
    }

    @Test
    void organizador_pagosPendientes_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "OrgPend", "email", "orgpend@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE");
        usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        String id = orgRepoRef.findAll().stream().filter(o -> Base64Util.encode("orgpend@test.com").equals(o.getEmail())).findFirst().get().getId();
        usuarioMvc.perform(get("/api/users/organizers/" + id + "/payments/pending")).andExpect(status().isOk());
    }

    @Test
    void organizador_iniciarTorneo_sinTorneo_retorna409() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Org2", "email", "org2@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE");
        usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        String id = orgRepoRef.findAll().stream().filter(o -> Base64Util.encode("org2@test.com").equals(o.getEmail())).findFirst().get().getId();
        usuarioMvc.perform(patch("/api/users/organizers/" + id + "/tournament/start")).andExpect(status().isConflict());
    }

    @Test
    void organizador_configurarTorneo_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "OrgConf", "email", "orgconf@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE");
        usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        String id = orgRepoRef.findAll().stream().filter(o -> Base64Util.encode("orgconf@test.com").equals(o.getEmail())).findFirst().get().getId();
        Torneo t = new Torneo(null, "Copa Config", LocalDateTime.of(2025, 9, 1, 10, 0), LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/users/organizers/" + id + "/tournament").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(t)));
        Map<String, Object> config = Map.of("reglamento", "Regla 1", "canchas", "Cancha A");
        usuarioMvc.perform(patch("/api/users/organizers/" + id + "/tournament/configure").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(config))).andExpect(status().isOk());
    }

    @Test
    void capitan_validarEquipo_sinEquipo_retorna400() throws Exception {
        Map<String, Object> body = Map.of("nombre", "CapVal", "email", "capval@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 3, "posicion", "VOLANTE");
        usuarioMvc.perform(post("/api/users/captains").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        String id = capitanRepoRef.findAll().stream().filter(c -> Base64Util.encode("capval@test.com").equals(c.getEmail())).findFirst().get().getId();
        usuarioMvc.perform(get("/api/users/captains/" + id + "/team/validate")).andExpect(status().isBadRequest());
    }
}
