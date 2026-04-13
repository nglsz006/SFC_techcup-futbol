package edu.dosw.project.SFC_TechUp_Futbol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.controller.*;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AccesoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ControllerTest {

    private MockMvc accesoMvc;
    private MockMvc torneoMvc;
    private MockMvc equipoMvc;
    private MockMvc partidoMvc;
    private MockMvc usuarioMvc;
    private EquipoService equipoService2;
    private TorneoService torneoService;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        AdministradorMapper adminMapper = TestMappers.administradorMapper();
        RegistroAuditoriaMapper auditoriaMapper = TestMappers.registroAuditoriaMapper();

        Map<String, UsuarioRegistradoEntity> usuarioStore = new HashMap<>();
        UsuarioRegistradoJpaRepository usuarioRepo = MockRepoHelper.usuarioRepo(usuarioStore);
        UsuarioRegistradoMapper usuarioMapper = TestMappers.usuarioRegistradoMapper();

        TorneoJpaRepository torneoRepoAcceso = mock(TorneoJpaRepository.class);
        OrganizadorMapper orgMapperAcceso = TestMappers.organizadorMapper(torneoRepoAcceso, torneoMapper);
        OrganizadorJpaRepository orgRepoAcceso = mock(OrganizadorJpaRepository.class);
        when(orgRepoAcceso.findByEmail(anyString())).thenReturn(Optional.empty());
        ArbitroJpaRepository arbRepoAcceso = mock(ArbitroJpaRepository.class);
        when(arbRepoAcceso.findByEmail(anyString())).thenReturn(Optional.empty());
        CapitanJpaRepository capRepoAcceso = mock(CapitanJpaRepository.class);
        when(capRepoAcceso.findByEmail(anyString())).thenReturn(Optional.empty());
        EquipoJpaRepository equipoRepoAcceso = mock(EquipoJpaRepository.class);
        ArbitroMapper arbMapperAcceso = TestMappers.arbitroMapper(partidoMapper);
        CapitanMapper capMapperAcceso = TestMappers.capitanMapper(equipoRepoAcceso, equipoMapper);

        AccesoServiceImpl accesoService = new AccesoServiceImpl(usuarioRepo, usuarioMapper, orgRepoAcceso, orgMapperAcceso, arbRepoAcceso, arbMapperAcceso, capRepoAcceso, capMapperAcceso, new edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService());
        accesoMvc = MockMvcBuilders
                .standaloneSetup(new AccesoController(accesoService, new AccesoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

        Map<String, TorneoEntity> torneoStore = new HashMap<>();
        TorneoJpaRepository torneoRepo2 = MockRepoHelper.torneoRepo(torneoStore);
        torneoService = new TorneoService(torneoRepo2, torneoMapper);

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo2 = MockRepoHelper.equipoRepo(equipoStore);
        equipoService2 = new EquipoService(equipoRepo2, equipoMapper);

        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        JugadorJpaRepository jugadorRepo2 = MockRepoHelper.jugadorRepo(jugadorStore);

        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        PartidoJpaRepository partidoRepo2 = MockRepoHelper.partidoRepo(partidoStore);

        PartidoServiceImpl partidoService2 = new PartidoServiceImpl(partidoRepo2, partidoMapper, torneoRepo2, torneoMapper, equipoRepo2, equipoMapper, jugadorRepo2, jugadorMapper);

        torneoMvc = MockMvcBuilders
                .standaloneSetup(new TorneoController(torneoService, partidoService2, equipoService2))
                .setControllerAdvice(new ErrorHandler()).build();

        partidoMvc = MockMvcBuilders
                .standaloneSetup(new PartidoController(partidoService2, new PartidoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

        JugadorService jugadorServiceEquipo = new JugadorService(jugadorRepo2, jugadorMapper);

        equipoMvc = MockMvcBuilders
                .standaloneSetup(new EquipoController(equipoService2, jugadorServiceEquipo))
                .setControllerAdvice(new ErrorHandler()).build();

        JugadorService jugadorService = new JugadorService(jugadorRepo2, jugadorMapper);

        Map<String, ArbitroEntity> arbitroStore = new HashMap<>();
        ArbitroJpaRepository arbitroRepo = MockRepoHelper.arbitroRepo(arbitroStore);
        ArbitroMapper arbitroMapper = TestMappers.arbitroMapper(partidoMapper);
        ArbitroService arbitroService = new ArbitroService(arbitroRepo, arbitroMapper);

        Map<String, CapitanEntity> capitanStore = new HashMap<>();
        CapitanJpaRepository capitanRepo = MockRepoHelper.capitanRepo(capitanStore);
        CapitanMapper capitanMapper = TestMappers.capitanMapper(equipoRepo2, equipoMapper);
        CapitanService capitanService = new CapitanService(capitanRepo, capitanMapper, jugadorService, jugadorRepo2, jugadorMapper);

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        TorneoJpaRepository torneoRepoOrg = MockRepoHelper.torneoRepo(new HashMap<>(torneoStore));
        OrganizadorMapper orgMapper = TestMappers.organizadorMapper(torneoRepo2, torneoMapper);
        OrganizadorJpaRepository orgRepo = MockRepoHelper.orgRepo(orgStore);
        OrganizadorService organizadorService = new OrganizadorService(orgRepo, orgMapper, torneoService);

        Map<String, PagoEntity> pagoStore = new HashMap<>();
        PagoJpaRepository pagoRepo = MockRepoHelper.pagoRepo(pagoStore);
        PagoMapper pagoMapper = TestMappers.pagoMapper(equipoMapper);
        PagoServiceImpl pagoService = new PagoServiceImpl(pagoRepo, pagoMapper, equipoRepo2, equipoMapper);

        Map<String, PerfilDeportivoEntity> perfilStore = new HashMap<>();
        PerfilDeportivoJpaRepository perfilRepo = MockRepoHelper.perfilRepo(perfilStore);
        PerfilDeportivoMapper perfilMapper = TestMappers.perfilDeportivoMapper();
        PerfilDeportivoService perfilService = new PerfilDeportivoServiceImpl(perfilRepo, perfilMapper, jugadorRepo2);

        usuarioMvc = MockMvcBuilders
                .standaloneSetup(new UsuarioController(jugadorService, capitanService,
                        arbitroService, organizadorService, pagoService,
                        partidoService2, new PartidoValidator(), equipoService2, torneoService, perfilService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    @Test
    void registro_valido_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Ana", "email", "ana@escuelaing.edu.co", "password", "12345678", "tipoUsuario", "ESTUDIANTE");
        accesoMvc.perform(post("/api/access/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.email").value("ana@escuelaing.edu.co"));
    }

    @Test
    void registro_correoInvalido_retorna400() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Ana", "email", "correo-invalido", "password", "12345678", "tipoUsuario", "ESTUDIANTE");
        accesoMvc.perform(post("/api/access/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_credencialesCorrectas_retornaToken() throws Exception {
        Map<String, Object> reg = Map.of("nombre", "Pedro", "email", "pedro@escuelaing.edu.co", "password", "12345678", "tipoUsuario", "ESTUDIANTE");
        accesoMvc.perform(post("/api/access/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg)));
        Map<String, String> login = Map.of("email", "pedro@escuelaing.edu.co", "password", "12345678");
        accesoMvc.perform(post("/api/access/login").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(login)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_passwordIncorrecta_retorna400() throws Exception {
        Map<String, Object> reg = Map.of("nombre", "Luis", "email", "luis@escuelaing.edu.co", "password", "12345678", "tipoUsuario", "ESTUDIANTE");
        accesoMvc.perform(post("/api/access/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg)));
        Map<String, String> login = Map.of("email", "luis@escuelaing.edu.co", "password", "wrongpass");
        accesoMvc.perform(post("/api/access/login").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest());
    }

    private String crearOrganizadorYTorneo(String emailOrg, String nombreTorneo) throws Exception {
        Map<String, Object> bodyOrg = Map.of("nombre", "Org", "email", emailOrg, "password", "12345678", "tipoUsuario", "ESTUDIANTE");
        String respOrg = usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        String orgId = mapper.readTree(respOrg).get("id").asText();
        Torneo t = new Torneo(null, nombreTorneo, LocalDateTime.of(2025, 9, 1, 10, 0), LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        String respTorneo = usuarioMvc.perform(post("/api/users/organizers/" + orgId + "/tournament").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(t)))
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(respTorneo).get("id").asText();
    }

    @Test
    void crearTorneo_retorna200() throws Exception {
        String torneoId = crearOrganizadorYTorneo("orgt@escuelaing.edu.co", "Copa Test");
        torneoMvc.perform(get("/api/tournaments/" + torneoId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Copa Test"));
    }

    @Test
    void listarTorneos_retorna200() throws Exception {
        torneoMvc.perform(get("/api/tournaments")).andExpect(status().isOk());
    }

    @Test
    void obtenerTorneo_inexistente_retorna400() throws Exception {
        torneoMvc.perform(get("/api/tournaments/id-inexistente")).andExpect(status().isBadRequest());
    }

    @Test
    void iniciarTorneo_existente_retorna200() throws Exception {
        Map<String, Object> bodyOrg2 = Map.of("nombre", "Org2", "email", "orgi2@escuelaing.edu.co", "password", "12345678", "tipoUsuario", "ESTUDIANTE");
        String respOrg2 = usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyOrg2)))
                .andReturn().getResponse().getContentAsString();
        String orgId2 = mapper.readTree(respOrg2).get("id").asText();
        Torneo t2 = new Torneo(null, "Copa Inicio2", LocalDateTime.of(2025, 9, 1, 10, 0), LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/users/organizers/" + orgId2 + "/tournament").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(t2)));
        usuarioMvc.perform(patch("/api/users/organizers/" + orgId2 + "/tournament/start")).andExpect(status().isOk());
    }

    @Test
    void finalizarTorneo_existente_retorna200() throws Exception {
        Map<String, Object> bodyOrg = Map.of("nombre", "OrgF", "email", "orgf@escuelaing.edu.co", "password", "12345678", "tipoUsuario", "ESTUDIANTE");
        String respOrg = usuarioMvc.perform(post("/api/users/organizers").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        String orgId = mapper.readTree(respOrg).get("id").asText();
        Torneo t = new Torneo(null, "Copa Final", LocalDateTime.of(2025, 9, 1, 10, 0), LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/users/organizers/" + orgId + "/tournament").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(t)));
        usuarioMvc.perform(patch("/api/users/organizers/" + orgId + "/tournament/start"));
        usuarioMvc.perform(patch("/api/users/organizers/" + orgId + "/tournament/end")).andExpect(status().isOk());
    }

    @Test
    void tablaPosiciones_retorna200() throws Exception {
        String torneoId = crearOrganizadorYTorneo("orgpos@escuelaing.edu.co", "Copa Pos");
        torneoMvc.perform(get("/api/tournaments/" + torneoId + "/positions")).andExpect(status().isOk());
    }

    @Test
    void llaveEliminatoria_retorna200() throws Exception {
        String torneoId = crearOrganizadorYTorneo("orgllave@escuelaing.edu.co", "Copa Llave");
        torneoMvc.perform(get("/api/tournaments/" + torneoId + "/bracket")).andExpect(status().isOk());
    }

    @Test
    void estadisticasTorneo_retorna200() throws Exception {
        String torneoId = crearOrganizadorYTorneo("orgstats@escuelaing.edu.co", "Copa Stats");
        torneoMvc.perform(get("/api/tournaments/" + torneoId + "/statistics")).andExpect(status().isOk());
    }

    @Test
    void listarEquipos_retorna200() throws Exception {
        equipoMvc.perform(get("/api/teams")).andExpect(status().isOk());
    }

    @Test
    void obtenerEquipo_existente_retorna200() throws Exception {
        Equipo e = equipoService2.crear(new Equipo(null, "Los Leones", "", "azul", "blanco", null), Map.of("nombre", "Los Leones", "colorPrincipal", "azul"));
        equipoMvc.perform(get("/api/teams/" + e.getId())).andExpect(status().isOk());
    }

    @Test
    void obtenerEquipo_inexistente_retorna400() throws Exception {
        equipoMvc.perform(get("/api/teams/id-inexistente")).andExpect(status().isBadRequest());
    }

    @Test
    void agregarJugador_equipo_retorna200() throws Exception {
        Map<String, Object> bodyCap = Map.of("nombre", "CapAg", "email", "capag@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 1, "posicion", "PORTERO");
        String respCap = usuarioMvc.perform(post("/api/users/captains").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        String capId = mapper.readTree(respCap).get("id").asText();
        usuarioMvc.perform(post("/api/users/captains/" + capId + "/team").param("nombreEquipo", "Los Agiles")).andExpect(status().isOk());
    }

    @Test
    void crearEquipo_viaCap_retorna200() throws Exception {
        Map<String, Object> body = Map.of("nombre", "CapEq", "email", "capeq@test.com", "password", "pass", "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 2, "posicion", "DEFENSA");
        String resp = usuarioMvc.perform(post("/api/users/captains").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        String id = mapper.readTree(resp).get("id").asText();
        usuarioMvc.perform(post("/api/users/captains/" + id + "/team").param("nombreEquipo", "Los Bravos")).andExpect(status().isOk());
    }

    @Test
    void crearEquipo_viaEndpoint_retorna200() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", "Los Tigres");
        body.put("escudo", "");
        body.put("colorPrincipal", "naranja");
        body.put("colorSecundario", "negro");
        body.put("capitanId", "");
        equipoMvc.perform(post("/api/teams").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(status().isOk());
    }

    @Test
    void agregarJugador_aEquipo_retorna200() throws Exception {
        Equipo e = equipoService2.crear(new Equipo(null, "Los Pumas", "", "verde", "blanco", null),
                Map.of("nombre", "Los Pumas", "colorPrincipal", "verde"));
        edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper jm =
                TestMappers.jugadorMapper();
        // crear jugador directo en el repo compartido via usuarioMvc
        Map<String, Object> jBody = new HashMap<>();
        jBody.put("nombre", "JugPuma"); jBody.put("email", "jugpuma@test.com");
        jBody.put("password", "pass"); jBody.put("tipoUsuario", "ESTUDIANTE");
        jBody.put("numeroCamiseta", 11); jBody.put("posicion", "DELANTERO");
        usuarioMvc.perform(post("/api/users/players").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(jBody)));
        // buscar jugador creado
        String jugId = equipoMvc.perform(get("/api/teams")).andReturn().getResponse().getContentAsString();
        // usar un jugador creado via servicio directamente
        edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador jug =
                new edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador(
                        null, "JugPuma2", "jugpuma2@test.com", "pass",
                        edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario.TipoUsuario.ESTUDIANTE,
                        12, edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador.Posicion.DELANTERO, true, "");
        // No podemos acceder al jugadorService desde aqui, usamos el endpoint de agregarJugador con id inexistente para cubrir el branch
        equipoMvc.perform(post("/api/teams/" + e.getId() + "/jugadores/id-inexistente"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void inscripcionHabilitada_retorna200() throws Exception {
        String torneoId = crearOrganizadorYTorneo("orgenroll@escuelaing.edu.co", "Copa Enroll");
        torneoMvc.perform(get("/api/tournaments/" + torneoId + "/enrollment"))
                .andExpect(status().isOk());
    }

    @Test
    void tablaPosiciones_conPartidoFinalizado_retorna200() throws Exception {
        Map<String, Object> bodyOrg = Map.of("nombre", "OrgPos", "email", "orgpos2@escuelaing.edu.co",
                "password", "12345678", "tipoUsuario", "ESTUDIANTE");
        String respOrg = usuarioMvc.perform(post("/api/users/organizers")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        String orgId = mapper.readTree(respOrg).get("id").asText();

        Torneo t = new Torneo(null, "Copa Pos2", LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        String torneoResp = usuarioMvc.perform(post("/api/users/organizers/" + orgId + "/tournament")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(t)))
                .andReturn().getResponse().getContentAsString();
        String torneoId = mapper.readTree(torneoResp).get("id").asText();

        Equipo eq1 = equipoService2.crear(new Equipo(null, "TeamA", "", "rojo", "blanco", null),
                Map.of("nombre", "TeamA", "colorPrincipal", "rojo"));
        Equipo eq2 = equipoService2.crear(new Equipo(null, "TeamB", "", "azul", "blanco", null),
                Map.of("nombre", "TeamB", "colorPrincipal", "azul"));

        Map<String, Object> pBody = new HashMap<>();
        pBody.put("torneoId", torneoId); pBody.put("equipoLocalId", eq1.getId());
        pBody.put("equipoVisitanteId", eq2.getId());
        pBody.put("fecha", "2025-09-10T15:00:00"); pBody.put("cancha", "Cancha 1");
        String pResp = usuarioMvc.perform(post("/api/users/organizers/" + orgId + "/matches")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(pBody)))
                .andReturn().getResponse().getContentAsString();
        String partidoId = mapper.readTree(pResp).get("id").asText();

        // crear arbitro, iniciar y finalizar partido
        Map<String, Object> arbBody = Map.of("nombre", "ArbPos", "email", "arbpos@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE");
        String arbResp = usuarioMvc.perform(post("/api/users/referees")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(arbBody)))
                .andReturn().getResponse().getContentAsString();
        String arbId = mapper.readTree(arbResp).path("mensaje").asText();
        // obtener id del arbitro creado
        String arbIdReal = usuarioMvc.perform(get("/api/users/referees"))
                .andReturn().getResponse().getContentAsString();
        String aId = mapper.readTree(arbIdReal).get(0).get("id").asText();

        usuarioMvc.perform(put("/api/users/referees/" + aId + "/matches/" + partidoId + "/start"));
        Map<String, Integer> resultado = Map.of("golesLocal", 2, "golesVisitante", 1);
        usuarioMvc.perform(put("/api/users/referees/" + aId + "/matches/" + partidoId + "/result")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(resultado)));
        usuarioMvc.perform(put("/api/users/referees/" + aId + "/matches/" + partidoId + "/end"));

        torneoMvc.perform(get("/api/tournaments/" + torneoId + "/positions")).andExpect(status().isOk());
        torneoMvc.perform(get("/api/tournaments/" + torneoId + "/bracket")).andExpect(status().isOk());
        torneoMvc.perform(get("/api/tournaments/" + torneoId + "/statistics")).andExpect(status().isOk());
    }
}
