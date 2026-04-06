package edu.dosw.project.SFC_TechUp_Futbol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.controller.*;
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
        TorneoMapper torneoMapper = new TorneoMapper();
        EquipoMapper equipoMapper = new EquipoMapper();
        JugadorMapper jugadorMapper = new JugadorMapper();
        PartidoMapper partidoMapper = new PartidoMapper(torneoMapper, equipoMapper, jugadorMapper);
        AdministradorMapper adminMapper = new AdministradorMapper();
        RegistroAuditoriaMapper auditoriaMapper = new RegistroAuditoriaMapper();

        Map<String, UsuarioRegistradoEntity> usuarioStore = new HashMap<>();
        UsuarioRegistradoJpaRepository usuarioRepo = MockRepoHelper.usuarioRepo(usuarioStore);
        UsuarioRegistradoMapper usuarioMapper = new UsuarioRegistradoMapper();

        TorneoJpaRepository torneoRepoAcceso = mock(TorneoJpaRepository.class);
        OrganizadorMapper orgMapperAcceso = new OrganizadorMapper(torneoRepoAcceso, torneoMapper);
        OrganizadorJpaRepository orgRepoAcceso = mock(OrganizadorJpaRepository.class);
        when(orgRepoAcceso.findByEmail(anyString())).thenReturn(Optional.empty());
        ArbitroJpaRepository arbRepoAcceso = mock(ArbitroJpaRepository.class);
        when(arbRepoAcceso.findByEmail(anyString())).thenReturn(Optional.empty());
        CapitanJpaRepository capRepoAcceso = mock(CapitanJpaRepository.class);
        when(capRepoAcceso.findByEmail(anyString())).thenReturn(Optional.empty());
        EquipoJpaRepository equipoRepoAcceso = mock(EquipoJpaRepository.class);
        ArbitroMapper arbMapperAcceso = new ArbitroMapper(partidoMapper);
        CapitanMapper capMapperAcceso = new CapitanMapper(equipoRepoAcceso, equipoMapper);

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
        ArbitroMapper arbitroMapper = new ArbitroMapper(partidoMapper);
        ArbitroService arbitroService = new ArbitroService(arbitroRepo, arbitroMapper);

        Map<String, CapitanEntity> capitanStore = new HashMap<>();
        CapitanJpaRepository capitanRepo = MockRepoHelper.capitanRepo(capitanStore);
        CapitanMapper capitanMapper = new CapitanMapper(equipoRepo2, equipoMapper);
        CapitanService capitanService = new CapitanService(capitanRepo, capitanMapper, jugadorService);

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        TorneoJpaRepository torneoRepoOrg = MockRepoHelper.torneoRepo(new HashMap<>(torneoStore));
        OrganizadorMapper orgMapper = new OrganizadorMapper(torneoRepo2, torneoMapper);
        OrganizadorJpaRepository orgRepo = MockRepoHelper.orgRepo(orgStore);
        OrganizadorService organizadorService = new OrganizadorService(orgRepo, orgMapper, torneoService);

        Map<String, PagoEntity> pagoStore = new HashMap<>();
        PagoJpaRepository pagoRepo = MockRepoHelper.pagoRepo(pagoStore);
        PagoMapper pagoMapper = new PagoMapper(equipoMapper);
        PagoServiceImpl pagoService = new PagoServiceImpl(pagoRepo, pagoMapper, equipoRepo2, equipoMapper);

        Map<String, PerfilDeportivoEntity> perfilStore = new HashMap<>();
        PerfilDeportivoJpaRepository perfilRepo = MockRepoHelper.perfilRepo(perfilStore);
        PerfilDeportivoMapper perfilMapper = new PerfilDeportivoMapper();
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
        Equipo e = equipoService2.crear(new Equipo(null, "Los Leones", "", "azul", "blanco", null), new HashMap<>());
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
}
