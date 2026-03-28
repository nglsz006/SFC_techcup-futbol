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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
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
        Map<Long, UsuarioRegistrado> usuarioStore = new HashMap<>();
        AtomicLong usuarioIdGen = new AtomicLong(1);
        UsuarioRegistradoRepository usuarioRepo = mock(UsuarioRegistradoRepository.class);
        when(usuarioRepo.save(any())).thenAnswer(inv -> {
            UsuarioRegistrado u = inv.getArgument(0);
            usuarioStore.put(usuarioIdGen.getAndIncrement(), u);
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

        Map<Integer, Torneo> torneoStore = new HashMap<>();
        AtomicInteger torneoIdGen = new AtomicInteger(1);
        torneoRepo2 = mock(TorneoRepository.class);
        when(torneoRepo2.save(any())).thenAnswer(inv -> {
            Torneo t = inv.getArgument(0);
            if (t.getId() == 0) t.setId(torneoIdGen.getAndIncrement());
            torneoStore.put(t.getId(), t);
            return t;
        });
        when(torneoRepo2.findById(anyInt())).thenAnswer(inv -> Optional.ofNullable(torneoStore.get(inv.<Integer>getArgument(0))));
        when(torneoRepo2.findAll()).thenAnswer(inv -> new ArrayList<>(torneoStore.values()));

        Map<Integer, Equipo> equipoStore = new HashMap<>();
        AtomicInteger equipoIdGen = new AtomicInteger(1);
        equipoRepo2 = mock(EquipoRepository.class);
        when(equipoRepo2.save(any())).thenAnswer(inv -> {
            Equipo e = inv.getArgument(0);
            if (e.getId() == 0) e.setId(equipoIdGen.getAndIncrement());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo2.findById(anyInt())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<Integer>getArgument(0))));
        when(equipoRepo2.findById(any(Long.class))).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(((Long) inv.getArgument(0)).intValue())));
        when(equipoRepo2.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));

        Map<Long, Jugador> jugadorStore = new HashMap<>();
        AtomicLong jugadorIdGen = new AtomicLong(1);
        jugadorRepo2 = mock(JugadorRepository.class);
        when(jugadorRepo2.save(any())).thenAnswer(inv -> {
            Jugador j = inv.getArgument(0);
            if (j.getId() == null) j.setId(jugadorIdGen.getAndIncrement());
            jugadorStore.put(j.getId(), j);
            return j;
        });
        when(jugadorRepo2.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<Long>getArgument(0))));
        when(jugadorRepo2.findAll()).thenAnswer(inv -> new ArrayList<>(jugadorStore.values()));

        Map<Long, Partido> partidoStore = new HashMap<>();
        AtomicLong partidoIdGen = new AtomicLong(1);
        partidoRepo2 = mock(PartidoRepository.class);
        when(partidoRepo2.save(any())).thenAnswer(inv -> {
            Partido p = inv.getArgument(0);
            if (p.getId() == null) p.setId(partidoIdGen.getAndIncrement());
            partidoStore.put(p.getId(), p);
            return p;
        });
        when(partidoRepo2.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(partidoStore.get(inv.<Long>getArgument(0))));
        when(partidoRepo2.findByTorneoId(anyLong())).thenAnswer(inv -> {
            Long tid = inv.getArgument(0);
            return partidoStore.values().stream().filter(p -> p.getTorneo() != null && p.getTorneo().getId() == tid.intValue()).collect(Collectors.toList());
        });
        when(partidoRepo2.findByEstado(any())).thenAnswer(inv -> {
            Partido.PartidoEstado estado = inv.getArgument(0);
            return partidoStore.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(partidoRepo2.findByEquipoLocalIdOrEquipoVisitanteId(anyLong(), anyLong())).thenAnswer(inv -> {
            Long lid = inv.getArgument(0); Long vid = inv.getArgument(1);
            return partidoStore.values().stream().filter(p ->
                    (p.getEquipoLocal() != null && p.getEquipoLocal().getId() == lid.intValue()) ||
                    (p.getEquipoVisitante() != null && p.getEquipoVisitante().getId() == vid.intValue())
            ).collect(Collectors.toList());
        });

        Map<Long, Capitan> capitanStore = new HashMap<>();
        AtomicLong capitanIdGen = new AtomicLong(1);
        CapitanRepository capitanRepo = mock(CapitanRepository.class);
        when(capitanRepo.save(any())).thenAnswer(inv -> {
            Capitan c = inv.getArgument(0);
            if (c.getId() == null) c.setId(capitanIdGen.getAndIncrement());
            capitanStore.put(c.getId(), c);
            return c;
        });
        when(capitanRepo.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(capitanStore.get(inv.<Long>getArgument(0))));
        when(capitanRepo.findAll()).thenAnswer(inv -> new ArrayList<>(capitanStore.values()));

        Map<Long, Arbitro> arbitroStore = new HashMap<>();
        AtomicLong arbitroIdGen = new AtomicLong(1);
        ArbitroRepository arbitroRepo = mock(ArbitroRepository.class);
        when(arbitroRepo.save(any())).thenAnswer(inv -> {
            Arbitro a = inv.getArgument(0);
            if (a.getId() == null) a.setId(arbitroIdGen.getAndIncrement());
            arbitroStore.put(a.getId(), a);
            return a;
        });
        when(arbitroRepo.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(arbitroStore.get(inv.<Long>getArgument(0))));
        when(arbitroRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return arbitroStore.values().stream().filter(a -> email.equals(a.getEmail())).findFirst();
        });
        when(arbitroRepo.findAll()).thenAnswer(inv -> new ArrayList<>(arbitroStore.values()));

        Map<Long, Organizador> orgStore = new HashMap<>();
        AtomicLong orgIdGen = new AtomicLong(1);
        OrganizadorRepository orgRepo = mock(OrganizadorRepository.class);
        when(orgRepo.save(any())).thenAnswer(inv -> {
            Organizador o = inv.getArgument(0);
            if (o.getId() == null) o.setId(orgIdGen.getAndIncrement());
            orgStore.put(o.getId(), o);
            return o;
        });
        when(orgRepo.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(orgStore.get(inv.<Long>getArgument(0))));
        when(orgRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return orgStore.values().stream().filter(o -> email.equals(o.getEmail())).findFirst();
        });
        when(orgRepo.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));

        Map<Long, Pago> pagoStore = new HashMap<>();
        AtomicLong pagoIdGen = new AtomicLong(1);
        PagoRepository pagoRepo = mock(PagoRepository.class);
        when(pagoRepo.save(any())).thenAnswer(inv -> {
            Pago p = inv.getArgument(0);
            if (p.getId() == null) p.setId(pagoIdGen.getAndIncrement());
            pagoStore.put(p.getId(), p);
            return p;
        });
        when(pagoRepo.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(pagoStore.get(inv.<Long>getArgument(0))));
        when(pagoRepo.findByEquipoId(anyLong())).thenAnswer(inv -> {
            Long eid = inv.getArgument(0);
            return pagoStore.values().stream().filter(p -> p.getEquipo() != null && p.getEquipo().getId() == eid.intValue()).collect(Collectors.toList());
        });
        when(pagoRepo.findByEstado(any())).thenAnswer(inv -> {
            Pago.PagoEstado estado = inv.getArgument(0);
            return pagoStore.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(pagoRepo.findByEquipoIdAndEstado(anyLong(), any())).thenAnswer(inv -> {
            Long eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().filter(p -> p.getEquipo() != null && p.getEquipo().getId() == eid.intValue() && p.getEstado() == estado).findFirst();
        });
        when(pagoRepo.existsByEquipoIdAndEstado(anyLong(), any())).thenAnswer(inv -> {
            Long eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStore.values().stream().anyMatch(p -> p.getEquipo() != null && p.getEquipo().getId() == eid.intValue() && p.getEstado() == estado);
        });

        Map<Long, PerfilDeportivo> perfilStore = new HashMap<>();
        AtomicLong perfilIdGen = new AtomicLong(1);
        PerfilDeportivoRepository perfilRepo = mock(PerfilDeportivoRepository.class);
        when(perfilRepo.save(any())).thenAnswer(inv -> {
            PerfilDeportivo pf = inv.getArgument(0);
            if (pf.getId() == null) pf.setId(perfilIdGen.getAndIncrement());
            perfilStore.put(pf.getId(), pf);
            return pf;
        });
        when(perfilRepo.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(perfilStore.get(inv.<Long>getArgument(0))));
        when(perfilRepo.findByJugadorId(anyLong())).thenAnswer(inv -> {
            Long jid = inv.getArgument(0);
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
        edu.dosw.project.SFC_TechUp_Futbol.core.service.PerfilDeportivoService perfilService =
                new edu.dosw.project.SFC_TechUp_Futbol.core.service.PerfilDeportivoServiceImpl(
                        perfilRepo, jugadorRepo2);
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
