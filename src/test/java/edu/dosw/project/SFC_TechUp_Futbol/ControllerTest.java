package edu.dosw.project.SFC_TechUp_Futbol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.controller.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AccesoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.controller.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ControllerTest {

    private MockMvc accesoMvc;
    private MockMvc torneoMvc;
    private MockMvc equipoMvc;
    private MockMvc partidoMvc;
    private MockMvc usuarioMvc;
    private TorneoRepository torneoRepo2;
    private EquipoRepository equipoRepo2;
    private JugadorRepository jugadorRepo2;
    private PartidoRepository partidoRepo2;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        Map<String, UsuarioRegistrado> usuarioStore = new HashMap<>();
        UsuarioRegistradoRepository usuarioRepo = mock(UsuarioRegistradoRepository.class);
        when(usuarioRepo.save(any())).thenAnswer(inv -> {
            UsuarioRegistrado u = inv.getArgument(0);
            if (u.getId() == null) u.setId(UUID.randomUUID().toString());
            usuarioStore.put(u.getId(), u);
            return u;
        });
        when(usuarioRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return usuarioStore.values().stream().filter(u -> email.equals(u.getEmail())).findFirst();
        });
        when(usuarioRepo.findAll()).thenAnswer(inv -> new ArrayList<>(usuarioStore.values()));

        AccesoServiceImpl accesoService = new AccesoServiceImpl(usuarioRepo, new edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService());
        accesoMvc = MockMvcBuilders
                .standaloneSetup(new AccesoController(accesoService, new AccesoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

        Map<String, Torneo> torneoStore = new HashMap<>();
        torneoRepo2 = mock(TorneoRepository.class);
        when(torneoRepo2.save(any())).thenAnswer(inv -> {
            Torneo t = inv.getArgument(0);
            if (t.getId() == null) t.setId(UUID.randomUUID().toString());
            torneoStore.put(t.getId(), t);
            return t;
        });
        when(torneoRepo2.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(torneoStore.get(inv.<String>getArgument(0))));
        when(torneoRepo2.findAll()).thenAnswer(inv -> new ArrayList<>(torneoStore.values()));

        Map<String, Equipo> equipoStore = new HashMap<>();
        equipoRepo2 = mock(EquipoRepository.class);
        when(equipoRepo2.save(any())).thenAnswer(inv -> {
            Equipo e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo2.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<String>getArgument(0))));
        when(equipoRepo2.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));

        Map<String, Jugador> jugadorStore = new HashMap<>();
        jugadorRepo2 = mock(JugadorRepository.class);
        when(jugadorRepo2.save(any())).thenAnswer(inv -> {
            Jugador j = inv.getArgument(0);
            if (j.getId() == null) j.setId(UUID.randomUUID().toString());
            jugadorStore.put(j.getId(), j);
            return j;
        });
        when(jugadorRepo2.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));
        when(jugadorRepo2.findAll()).thenAnswer(inv -> new ArrayList<>(jugadorStore.values()));

        Map<String, Partido> partidoStore = new HashMap<>();
        partidoRepo2 = mock(PartidoRepository.class);
        when(partidoRepo2.save(any())).thenAnswer(inv -> {
            Partido p = inv.getArgument(0);
            if (p.getId() == null) p.setId(UUID.randomUUID().toString());
            partidoStore.put(p.getId(), p);
            return p;
        });
        when(partidoRepo2.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(partidoStore.get(inv.<String>getArgument(0))));
        when(partidoRepo2.findByTorneoId(anyString())).thenAnswer(inv -> {
            String tid = inv.getArgument(0);
            return partidoStore.values().stream().filter(p -> p.getTorneo() != null && tid.equals(p.getTorneo().getId())).collect(Collectors.toList());
        });
        when(partidoRepo2.findByEstado(any())).thenAnswer(inv -> {
            Partido.PartidoEstado estado = inv.getArgument(0);
            return partidoStore.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(partidoRepo2.findByEquipoLocalIdOrEquipoVisitanteId(anyString(), anyString())).thenAnswer(inv -> {
            String lid = inv.getArgument(0); String vid = inv.getArgument(1);
            return partidoStore.values().stream().filter(p ->
                    (p.getEquipoLocal() != null && lid.equals(p.getEquipoLocal().getId())) ||
                    (p.getEquipoVisitante() != null && vid.equals(p.getEquipoVisitante().getId()))
            ).collect(Collectors.toList());
        });

        Map<String, Capitan> capitanStore = new HashMap<>();
        CapitanRepository capitanRepo = mock(CapitanRepository.class);
        when(capitanRepo.save(any())).thenAnswer(inv -> {
            Capitan c = inv.getArgument(0);
            if (c.getId() == null) c.setId(UUID.randomUUID().toString());
            capitanStore.put(c.getId(), c);
            return c;
        });
        when(capitanRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(capitanStore.get(inv.<String>getArgument(0))));
        when(capitanRepo.findAll()).thenAnswer(inv -> new ArrayList<>(capitanStore.values()));

        Map<String, Arbitro> arbitroStore = new HashMap<>();
        ArbitroRepository arbitroRepo = mock(ArbitroRepository.class);
        when(arbitroRepo.save(any())).thenAnswer(inv -> {
            Arbitro a = inv.getArgument(0);
            if (a.getId() == null) a.setId(UUID.randomUUID().toString());
            arbitroStore.put(a.getId(), a);
            return a;
        });
        when(arbitroRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(arbitroStore.get(inv.<String>getArgument(0))));
        when(arbitroRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return arbitroStore.values().stream().filter(a -> email.equals(a.getEmail())).findFirst();
        });
        when(arbitroRepo.findAll()).thenAnswer(inv -> new ArrayList<>(arbitroStore.values()));

        Map<String, Organizador> orgStore = new HashMap<>();
        OrganizadorRepository orgRepo = mock(OrganizadorRepository.class);
        when(orgRepo.save(any())).thenAnswer(inv -> {
            Organizador o = inv.getArgument(0);
            if (o.getId() == null) o.setId(UUID.randomUUID().toString());
            orgStore.put(o.getId(), o);
            return o;
        });
        when(orgRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(orgStore.get(inv.<String>getArgument(0))));
        when(orgRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return orgStore.values().stream().filter(o -> email.equals(o.getEmail())).findFirst();
        });
        when(orgRepo.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));

        Map<String, Pago> pagoStore = new HashMap<>();
        PagoRepository pagoRepo = mock(PagoRepository.class);
        when(pagoRepo.save(any())).thenAnswer(inv -> {
            Pago p = inv.getArgument(0);
            if (p.getId() == null) p.setId(UUID.randomUUID().toString());
            pagoStore.put(p.getId(), p);
            return p;
        });
        when(pagoRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(pagoStore.get(inv.<String>getArgument(0))));
        when(pagoRepo.findByEquipoId(anyString())).thenAnswer(inv -> {
            String eid = inv.getArgument(0);
            return pagoStore.values().stream().filter(p -> p.getEquipo() != null && eid.equals(p.getEquipo().getId())).collect(Collectors.toList());
        });
        when(pagoRepo.findByEstado(any())).thenAnswer(inv -> {
            Pago.PagoEstado estado = inv.getArgument(0);
            return pagoStore.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(pagoRepo.findByEquipoIdAndEstado(anyString(), any())).thenAnswer(inv -> {
            String eid = inv.getArgument(0); Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().filter(p -> p.getEquipo() != null && eid.equals(p.getEquipo().getId()) && p.getEstado() == estado).findFirst();
        });
        when(pagoRepo.existsByEquipoIdAndEstado(anyString(), any())).thenAnswer(inv -> {
            String eid = inv.getArgument(0); Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().anyMatch(p -> p.getEquipo() != null && eid.equals(p.getEquipo().getId()) && p.getEstado() == estado);
        });

        Map<String, PerfilDeportivo> perfilStore = new HashMap<>();
        PerfilDeportivoRepository perfilRepo = mock(PerfilDeportivoRepository.class);
        when(perfilRepo.save(any())).thenAnswer(inv -> {
            PerfilDeportivo pf = inv.getArgument(0);
            if (pf.getId() == null) pf.setId(UUID.randomUUID().toString());
            perfilStore.put(pf.getId(), pf);
            return pf;
        });
        when(perfilRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(perfilStore.get(inv.<String>getArgument(0))));
        when(perfilRepo.findByJugadorId(anyString())).thenAnswer(inv -> {
            String jid = inv.getArgument(0);
            return perfilStore.values().stream().filter(pf -> jid.equals(pf.getJugadorId())).findFirst();
        });

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
        ArbitroService arbitroService = new ArbitroService(arbitroRepo);
        CapitanService capitanService = new CapitanService(capitanRepo, jugadorService);
        OrganizadorService organizadorService = new OrganizadorService(orgRepo, torneoService);
        PagoServiceImpl pagoService = new PagoServiceImpl(pagoRepo, equipoRepo2);
        PartidoValidator partidoValidator = new PartidoValidator();
        PerfilDeportivoService perfilService = new PerfilDeportivoServiceImpl(perfilRepo, jugadorRepo2);
        usuarioMvc = MockMvcBuilders
                .standaloneSetup(new UsuarioController(jugadorService, jugadorRepo2, capitanService,
                        arbitroService, partidoRepo2, organizadorService, pagoService,
                        partidoService2, partidoValidator, equipoService2, torneoService, perfilService))
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
        Equipo e = equipoRepo2.save(new Equipo(null, "Los Leones", "", "azul", "blanco", null));
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
