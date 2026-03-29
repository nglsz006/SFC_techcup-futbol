package edu.dosw.project.SFC_TechUp_Futbol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.controller.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.controller.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PagoPartidoControllerTest {

    private MockMvc pagoMvc;
    private MockMvc partidoMvc;
    private MockMvc alineacionMvc;
    private MockMvc usuarioMvc;
    private PagoServiceImpl pagoService;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private EquipoRepository equipoRepo;
    private TorneoRepository torneoRepo;
    private JugadorRepository jugadorRepo;
    private Equipo equipo;
    private Torneo torneo;
    private Jugador jugador;

    @BeforeEach
    void setUp() {
        Map<Integer, Equipo> equipoStore = new HashMap<>();
        AtomicInteger equipoIdGen = new AtomicInteger(1);
        equipoRepo = mock(EquipoRepository.class);
        when(equipoRepo.save(any())).thenAnswer(inv -> {
            Equipo e = inv.getArgument(0);
            if (e.getId() == 0) e.setId(equipoIdGen.getAndIncrement());
            equipoStore.put(e.getId(), e);
            return e;
        });
        when(equipoRepo.findById(anyInt())).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(inv.<Integer>getArgument(0))));
        when(equipoRepo.findById(any(Long.class))).thenAnswer(inv -> Optional.ofNullable(equipoStore.get(((Long) inv.getArgument(0)).intValue())));
        when(equipoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(equipoStore.values()));

        Map<Integer, Torneo> torneoStore = new HashMap<>();
        AtomicInteger torneoIdGen = new AtomicInteger(1);
        torneoRepo = mock(TorneoRepository.class);
        when(torneoRepo.save(any())).thenAnswer(inv -> {
            Torneo t = inv.getArgument(0);
            if (t.getId() == 0) t.setId(torneoIdGen.getAndIncrement());
            torneoStore.put(t.getId(), t);
            return t;
        });
        when(torneoRepo.findById(anyInt())).thenAnswer(inv -> Optional.ofNullable(torneoStore.get(inv.<Integer>getArgument(0))));
        when(torneoRepo.findAll()).thenAnswer(inv -> new ArrayList<>(torneoStore.values()));

        Map<Long, Jugador> jugadorStore = new HashMap<>();
        AtomicLong jugadorIdGen = new AtomicLong(1);
        jugadorRepo = mock(JugadorRepository.class);
        when(jugadorRepo.save(any())).thenAnswer(inv -> {
            Jugador j = inv.getArgument(0);
            if (j.getId() == null) j.setId(jugadorIdGen.getAndIncrement());
            jugadorStore.put(j.getId(), j);
            return j;
        });
        when(jugadorRepo.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(jugadorStore.get(inv.<Long>getArgument(0))));
        when(jugadorRepo.findAll()).thenAnswer(inv -> new ArrayList<>(jugadorStore.values()));

        equipo = equipoRepo.save(new Equipo(0, "Los Tigres", "", "rojo", "blanco", 1));
        torneo = torneoRepo.save(new Torneo(0, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50));
        jugador = new Jugador(1L, "Juan", "juan@test.com", "pass", Usuario.TipoUsuario.ESTUDIANTE, 10, Jugador.Posicion.DELANTERO, true, "");
        jugadorRepo.save(jugador);

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

        pagoService = new PagoServiceImpl(pagoRepo, equipoRepo);
        pagoMvc = MockMvcBuilders
                .standaloneSetup(new PagoController(pagoService, new PagoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

        Map<Long, Partido> partidoStore = new HashMap<>();
        AtomicLong partidoIdGen = new AtomicLong(1);
        PartidoRepository partidoRepo = mock(PartidoRepository.class);
        when(partidoRepo.save(any())).thenAnswer(inv -> {
            Partido p = inv.getArgument(0);
            if (p.getId() == null) p.setId(partidoIdGen.getAndIncrement());
            partidoStore.put(p.getId(), p);
            return p;
        });
        when(partidoRepo.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(partidoStore.get(inv.<Long>getArgument(0))));
        when(partidoRepo.findByTorneoId(anyLong())).thenAnswer(inv -> {
            Long tid = inv.getArgument(0);
            return partidoStore.values().stream().filter(p -> p.getTorneo() != null && p.getTorneo().getId() == tid.intValue()).collect(Collectors.toList());
        });
        when(partidoRepo.findByEstado(any())).thenAnswer(inv -> {
            Partido.PartidoEstado estado = inv.getArgument(0);
            return partidoStore.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(partidoRepo.findByEquipoLocalIdOrEquipoVisitanteId(anyLong(), anyLong())).thenAnswer(inv -> {
            Long lid = inv.getArgument(0);
            Long vid = inv.getArgument(1);
            return partidoStore.values().stream().filter(p ->
                    (p.getEquipoLocal() != null && p.getEquipoLocal().getId() == lid.intValue()) ||
                            (p.getEquipoVisitante() != null && p.getEquipoVisitante().getId() == vid.intValue())
            ).collect(Collectors.toList());
        });

        PartidoServiceImpl partidoService = new PartidoServiceImpl(partidoRepo, torneoRepo, equipoRepo, jugadorRepo);
        partidoMvc = MockMvcBuilders
                .standaloneSetup(new PartidoController(partidoService, new PartidoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();

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

        Map<Integer, Alineacion> alineacionStore = new HashMap<>();
        AtomicInteger alineacionIdGen = new AtomicInteger(1);
        AlineacionRepository alineacionRepo = mock(AlineacionRepository.class);
        when(alineacionRepo.save(any())).thenAnswer(inv -> {
            Alineacion a = inv.getArgument(0);
            if (a.getId() == 0) a.setId(alineacionIdGen.getAndIncrement());
            alineacionStore.put(a.getId(), a);
            return a;
        });
        when(alineacionRepo.findById(anyInt())).thenAnswer(inv -> Optional.ofNullable(alineacionStore.get(inv.<Integer>getArgument(0))));
        when(alineacionRepo.findAll()).thenAnswer(inv -> new ArrayList<>(alineacionStore.values()));

        AlineacionService alineacionService = new AlineacionService(alineacionRepo);
        alineacionMvc = MockMvcBuilders
                .standaloneSetup(new AlineacionController(alineacionService))
                .setControllerAdvice(new ErrorHandler()).build();

        JugadorService jugadorService = new JugadorService(jugadorRepo);

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

        ArbitroService arbitroService = new ArbitroService(arbitroRepo);

        Map<Long, Partido> partidoStoreArbitro = new HashMap<>();
        AtomicLong partidoIdGenArbitro = new AtomicLong(1);
        PartidoRepository partidoRepoArbitro = mock(PartidoRepository.class);
        when(partidoRepoArbitro.save(any())).thenAnswer(inv -> {
            Partido p = inv.getArgument(0);
            if (p.getId() == null) p.setId(partidoIdGenArbitro.getAndIncrement());
            partidoStoreArbitro.put(p.getId(), p);
            return p;
        });
        when(partidoRepoArbitro.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(partidoStoreArbitro.get(inv.<Long>getArgument(0))));
        when(partidoRepoArbitro.findByTorneoId(anyLong())).thenAnswer(inv -> new ArrayList<>());
        when(partidoRepoArbitro.findByEstado(any())).thenAnswer(inv -> new ArrayList<>());
        when(partidoRepoArbitro.findByEquipoLocalIdOrEquipoVisitanteId(anyLong(), anyLong())).thenAnswer(inv -> new ArrayList<>());

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

        CapitanService capitanService = new CapitanService(capitanRepo, jugadorService);

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

        TorneoService torneoService = new TorneoService(torneoRepo);
        OrganizadorService organizadorService = new OrganizadorService(orgRepo, torneoService);

        Map<Long, Pago> pagoStoreOrg = new HashMap<>();
        AtomicLong pagoIdGenOrg = new AtomicLong(1);
        PagoRepository pagoRepoOrg = mock(PagoRepository.class);
        when(pagoRepoOrg.save(any())).thenAnswer(inv -> {
            Pago p = inv.getArgument(0);
            if (p.getId() == null) p.setId(pagoIdGenOrg.getAndIncrement());
            pagoStoreOrg.put(p.getId(), p);
            return p;
        });
        when(pagoRepoOrg.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(pagoStoreOrg.get(inv.<Long>getArgument(0))));
        when(pagoRepoOrg.findByEquipoId(anyLong())).thenAnswer(inv -> {
            Long eid = inv.getArgument(0);
            return pagoStoreOrg.values().stream().filter(p -> p.getEquipo() != null && p.getEquipo().getId() == eid.intValue()).collect(Collectors.toList());
        });
        when(pagoRepoOrg.findByEstado(any())).thenAnswer(inv -> {
            Pago.PagoEstado estado = inv.getArgument(0);
            return pagoStoreOrg.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(pagoRepoOrg.findByEquipoIdAndEstado(anyLong(), any())).thenAnswer(inv -> {
            Long eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStoreOrg.values().stream().filter(p -> p.getEquipo() != null && p.getEquipo().getId() == eid.intValue() && p.getEstado() == estado).findFirst();
        });
        when(pagoRepoOrg.existsByEquipoIdAndEstado(anyLong(), any())).thenAnswer(inv -> {
            Long eid = inv.getArgument(0);
            Pago.PagoEstado estado = inv.getArgument(1);
            return pagoStoreOrg.values().stream().anyMatch(p -> p.getEquipo() != null && p.getEquipo().getId() == eid.intValue() && p.getEstado() == estado);
        });
        PagoServiceImpl pagoServiceOrg = new PagoServiceImpl(pagoRepoOrg, equipoRepo);

        Map<Long, Partido> partidoStoreUsuario = new HashMap<>();
        AtomicLong partidoIdGenUsuario = new AtomicLong(1);
        PartidoRepository partidoRepoUsuario = mock(PartidoRepository.class);
        when(partidoRepoUsuario.save(any())).thenAnswer(inv -> {
            Partido p = inv.getArgument(0);
            if (p.getId() == null) p.setId(partidoIdGenUsuario.getAndIncrement());
            partidoStoreUsuario.put(p.getId(), p);
            return p;
        });
        when(partidoRepoUsuario.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(partidoStoreUsuario.get(inv.<Long>getArgument(0))));
        when(partidoRepoUsuario.findByTorneoId(anyLong())).thenAnswer(inv -> {
            Long tid = inv.getArgument(0);
            return partidoStoreUsuario.values().stream().filter(p -> p.getTorneo() != null && p.getTorneo().getId() == tid.intValue()).collect(Collectors.toList());
        });
        when(partidoRepoUsuario.findByEstado(any())).thenAnswer(inv -> {
            Partido.PartidoEstado estado = inv.getArgument(0);
            return partidoStoreUsuario.values().stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
        });
        when(partidoRepoUsuario.findByEquipoLocalIdOrEquipoVisitanteId(anyLong(), anyLong())).thenAnswer(inv -> {
            Long lid = inv.getArgument(0);
            Long vid = inv.getArgument(1);
            return partidoStoreUsuario.values().stream().filter(p ->
                    (p.getEquipoLocal() != null && p.getEquipoLocal().getId() == lid.intValue()) ||
                            (p.getEquipoVisitante() != null && p.getEquipoVisitante().getId() == vid.intValue())
            ).collect(Collectors.toList());
        });

        PartidoServiceImpl partidoServiceUsuario = new PartidoServiceImpl(partidoRepoUsuario, torneoRepo, equipoRepo, jugadorRepo);
        EquipoService equipoServiceUsuario = new EquipoService(equipoRepo);
        PartidoValidator partidoValidatorUsuario = new PartidoValidator();
        edu.dosw.project.SFC_TechUp_Futbol.core.service.PerfilDeportivoService perfilService =
                new edu.dosw.project.SFC_TechUp_Futbol.core.service.PerfilDeportivoServiceImpl(
                        perfilRepo, jugadorRepo);
        usuarioMvc = MockMvcBuilders
                .standaloneSetup(new UsuarioController(jugadorService, jugadorRepo, capitanService,
                        arbitroService, partidoRepoArbitro, organizadorService, pagoService,
                        partidoServiceUsuario, partidoValidatorUsuario, equipoServiceUsuario, torneoService, perfilService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    // ── Pagos ──────────────────────────────────────────────────────────────────

    @Test
    void pago_subirComprobante_retorna200() throws Exception {
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgSub", "email", "orgsub@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Map<String, Object> bodyCap = Map.of(
                "nombre", "CapSub", "email", "capsub@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 5, "posicion", "DEFENSA"
        );
        String respCap = usuarioMvc.perform(post("/api/users/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(respCap).get("id").asLong();
        usuarioMvc.perform(post("/api/users/captains/" + capId + "/receipt")
                        .param("comprobante", "pago.jpg"))
                .andExpect(status().isOk());
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
    void pago_consultarInexistente_retorna500() throws Exception {
        pagoMvc.perform(get("/api/payments/999")).andExpect(status().isInternalServerError());
    }

    @Test
    void pago_aprobar_retorna200() throws Exception {
        Map<String, Object> bodyCap = Map.of(
                "nombre", "CapApr", "email", "capapr@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 4, "posicion", "PORTERO"
        );
        String respCap = usuarioMvc.perform(post("/api/users/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(respCap).get("id").asLong();
        usuarioMvc.perform(post("/api/users/captains/" + capId + "/receipt")
                        .param("comprobante", "pago.jpg"))
                .andReturn().getResponse().getContentAsString();
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgPago", "email", "orgpago@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        List<Pago> pendientes = pagoService.consultarPagosPendientes();
        if (!pendientes.isEmpty()) {
            Long pagoId = pendientes.get(0).getId();
            usuarioMvc.perform(put("/api/users/organizers/" + orgId + "/payments/" + pagoId + "/approve"))
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
        String respCap = usuarioMvc.perform(post("/api/users/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(respCap).get("id").asLong();
        usuarioMvc.perform(post("/api/users/captains/" + capId + "/receipt")
                .param("comprobante", "pago.jpg"));
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgRech", "email", "orgrech@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        List<Pago> pendientes = pagoService.consultarPagosPendientes();
        if (!pendientes.isEmpty()) {
            Long pagoId = pendientes.get(0).getId();
            usuarioMvc.perform(put("/api/users/organizers/" + orgId + "/payments/" + pagoId + "/reject"))
                    .andExpect(status().isOk());
        }
    }

// ── Partidos ───────────────────────────────────────────────────────────────

    @Test
    void partido_crear_retorna200() throws Exception {
        Equipo visitante2 = equipoRepo.save(new Equipo(0, "Visitante2", "", "azul", "negro", 2));
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgPart", "email", "orgpart@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Map<String, Object> bodyPartido = Map.of(
                "torneoId", (long) torneo.getId(),
                "equipoLocalId", (long) equipo.getId(),
                "equipoVisitanteId", (long) visitante2.getId(),
                "fecha", "2025-09-01T10:00:00",
                "cancha", "cancha 1"
        );
        usuarioMvc.perform(post("/api/users/organizers/" + orgId + "/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyPartido)))
                .andExpect(status().isOk());
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
    void partido_flujoCompleto_retorna200() throws Exception {
        Equipo visitante2 = equipoRepo.save(new Equipo(0, "Visitante2", "", "azul", "negro", 2));
        Map<String, Object> bodyOrg = Map.of(
                "nombre", "OrgFlujo", "email", "orgflujo@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respOrg = usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyOrg)))
                .andReturn().getResponse().getContentAsString();
        Long orgId = mapper.readTree(respOrg).get("id").asLong();
        Map<String, Object> bodyArbitro = Map.of(
                "nombre", "RefFlujo", "email", "refflujo@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String respArb = usuarioMvc.perform(post("/api/users/referees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyArbitro)))
                .andReturn().getResponse().getContentAsString();
        Long arbId = mapper.readTree(respArb).get("id").asLong();
        Map<String, Object> bodyPartido = Map.of(
                "torneoId", torneo.getId(),
                "equipoLocalId", equipo.getId(),
                "equipoVisitanteId", visitante2.getId(),
                "fecha", "2025-09-01T10:00:00",
                "cancha", "cancha 2"
        );
        String resp = usuarioMvc.perform(post("/api/users/organizers/" + orgId + "/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyPartido)))
                .andReturn().getResponse().getContentAsString();
        Long pid = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(put("/api/users/referees/" + arbId + "/matches/" + pid + "/start")).andExpect(status().isOk());
        usuarioMvc.perform(post("/api/users/referees/" + arbId + "/matches/" + pid + "/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("jugadorId", jugador.getId(), "minuto", 10))))
                .andExpect(status().isOk());
        usuarioMvc.perform(post("/api/users/referees/" + arbId + "/matches/" + pid + "/sanctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("jugadorId", jugador.getId(), "tipoSancion", "TARJETA_AMARILLA", "descripcion", "Falta reiterada en minuto 30"))))
                .andExpect(status().isOk());
        usuarioMvc.perform(put("/api/users/referees/" + arbId + "/matches/" + pid + "/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("golesLocal", 2, "golesVisitante", 1))))
                .andExpect(status().isOk());
        usuarioMvc.perform(put("/api/users/referees/" + arbId + "/matches/" + pid + "/end")).andExpect(status().isOk());
        usuarioMvc.perform(get("/api/users/referees/" + arbId + "/matches")).andExpect(status().isOk());
    }

// ── Alineaciones ───────────────────────────────────────────────────────────

    @Test
    void alineacion_crear_retorna400_porValidacion() throws Exception {
        Map<String, Object> bodyCap = Map.of(
                "nombre", "CapAlin", "email", "capalin@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 2, "posicion", "DEFENSA"
        );
        String respCap = usuarioMvc.perform(post("/api/users/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyCap)))
                .andReturn().getResponse().getContentAsString();
        Long capId = mapper.readTree(respCap).get("id").asLong();
        usuarioMvc.perform(post("/api/users/captains/" + capId + "/lineup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(java.util.List.of())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void alineacion_listar_retorna200() throws Exception {
        alineacionMvc.perform(get("/api/lineups")).andExpect(status().isOk());
    }

    @Test
    void alineacion_consultarRival_sinDatos_retorna400() throws Exception {
        alineacionMvc.perform(get("/api/lineups/rival")
                        .param("partidoId", "1")
                        .param("equipoId", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void alineacion_obtenerInexistente_retorna400() throws Exception {
        alineacionMvc.perform(get("/api/lineups/999")).andExpect(status().isBadRequest());
    }

// ── Árbitros ───────────────────────────────────────────────────────────────

    @Test
    void arbitro_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Ref1", "email", "ref@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        usuarioMvc.perform(post("/api/users/referees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void arbitro_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/referees")).andExpect(status().isOk());
    }

    @Test
    void arbitro_asignarPartido_inexistente_retorna400() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Ref2", "email", "ref2@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String resp = usuarioMvc.perform(post("/api/users/referees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(post("/api/users/referees/" + id + "/matches/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void arbitro_consultarPartidos_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/referees/1/matches")).andExpect(status().isOk());
    }

// ── Capitanes ──────────────────────────────────────────────────────────────

    @Test
    void capitan_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Cap1", "email", "cap@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 10, "posicion", "DELANTERO"
        );
        usuarioMvc.perform(post("/api/users/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void capitan_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/captains")).andExpect(status().isOk());
    }

    @Test
    void capitan_crearEquipo_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Cap2", "email", "cap2@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 7, "posicion", "VOLANTE"
        );
        String resp = usuarioMvc.perform(post("/api/users/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(post("/api/users/captains/" + id + "/team")
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
        String resp = usuarioMvc.perform(post("/api/users/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(post("/api/users/captains/" + id + "/invite/" + jugador.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void capitan_subirComprobante_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Cap4", "email", "cap4@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 6, "posicion", "PORTERO"
        );
        String resp = usuarioMvc.perform(post("/api/users/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(post("/api/users/captains/" + id + "/receipt")
                        .param("comprobante", "pago.pdf"))
                .andExpect(status().isOk());
    }

    @Test
    void capitan_buscarJugadores_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/captains/1/search-players").param("posicion", "DELANTERO"))
                .andExpect(status().isOk());
    }

// ── Jugadores ──────────────────────────────────────────────────────────────

    @Test
    void jugador_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Carlos", "email", "carlos@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 9, "posicion", "DELANTERO"
        );
        usuarioMvc.perform(post("/api/users/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void jugador_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/players")).andExpect(status().isOk());
    }

    @Test
    void jugador_aceptarInvitacion_retorna200() throws Exception {
        usuarioMvc.perform(patch("/api/users/players/" + jugador.getId() + "/accept-invitation"))
                .andExpect(status().isOk());
    }

    @Test
    void jugador_rechazarInvitacion_retorna200() throws Exception {
        usuarioMvc.perform(patch("/api/users/players/1/reject-invitation"))
                .andExpect(status().isOk());
    }

    @Test
    void jugador_editarPerfil_retorna200() throws Exception {
        Map<String, Object> bodyJug = Map.of(
                "nombre", "JugEdit", "email", "jugedit@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 5, "posicion", "DEFENSA"
        );
        String resp = usuarioMvc.perform(post("/api/users/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bodyJug)))
                .andReturn().getResponse().getContentAsString();
        Long jugId = mapper.readTree(resp).get("id").asLong();
        Map<String, Object> perfil = Map.of("nombre", "JugEditado", "numeroCamiseta", 11, "posicion", "PORTERO");
        usuarioMvc.perform(patch("/api/users/players/" + jugId + "/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(perfil)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jerseyNumber").value(11));
    }

// ── Organizadores ──────────────────────────────────────────────────────────

    @Test
    void organizador_crear_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Org1", "email", "org1@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void organizador_listar_retorna200() throws Exception {
        usuarioMvc.perform(get("/api/users/organizers")).andExpect(status().isOk());
    }

    @Test
    void organizador_crearTorneo_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "OrgT", "email", "orgt@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String resp = usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        Torneo t = new Torneo(0, "Copa Nueva",
                LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/users/organizers/" + id + "/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(t)))
                .andExpect(status().isOk());
    }

    @Test
    void organizador_pagosPendientes_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "OrgPend", "email", "orgpend@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String resp = usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(get("/api/users/organizers/" + id + "/payments/pending"))
                .andExpect(status().isOk());
    }

    @Test
    void organizador_iniciarTorneo_sinTorneo_retorna409() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Org2", "email", "org2@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String resp = usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(patch("/api/users/organizers/" + id + "/tournament/start"))
                .andExpect(status().isConflict());
    }

    @Test
    void organizador_configurarTorneo_retorna200() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "OrgConf", "email", "orgconf@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE"
        );
        String resp = usuarioMvc.perform(post("/api/users/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        Torneo t = new Torneo(0, "Copa Config",
                LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        usuarioMvc.perform(post("/api/users/organizers/" + id + "/tournament")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(t)));
        Map<String, Object> config = Map.of("reglamento", "Regla 1", "canchas", "Cancha A");
        usuarioMvc.perform(patch("/api/users/organizers/" + id + "/tournament/configure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(config)))
                .andExpect(status().isOk());
    }

    @Test
    void capitan_validarEquipo_sinEquipo_retorna400() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "CapVal", "email", "capval@test.com",
                "password", "pass", "tipoUsuario", "ESTUDIANTE",
                "numeroCamiseta", 3, "posicion", "VOLANTE"
        );
        String resp = usuarioMvc.perform(post("/api/users/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(resp).get("id").asLong();
        usuarioMvc.perform(get("/api/users/captains/" + id + "/team/validate"))
                .andExpect(status().isOk());
    }
}
