package edu.dosw.project.SFC_TechUp_Futbol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TorneoControllerExtendedTest {

    private MockMvc mvc;
    private TorneoService torneoService;
    private PartidoServiceImpl partidoService;
    private EquipoService equipoService;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);

        Map<String, TorneoEntity> torneoStore = new HashMap<>();
        TorneoJpaRepository torneoRepo = MockRepoHelper.torneoRepo(torneoStore);
        torneoService = new TorneoService(torneoRepo, torneoMapper);

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = MockRepoHelper.equipoRepo(equipoStore);
        equipoService = new EquipoService(equipoRepo, equipoMapper);

        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        JugadorJpaRepository jugadorRepo = MockRepoHelper.jugadorRepo(jugadorStore);

        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        PartidoJpaRepository partidoRepo = MockRepoHelper.partidoRepo(partidoStore);

        partidoService = new PartidoServiceImpl(partidoRepo, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepo, jugadorMapper);

        mvc = MockMvcBuilders
                .standaloneSetup(new TorneoController(torneoService, partidoService, equipoService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    private String crearTorneo() throws Exception {
        Torneo t = new Torneo(null, "Copa Test", LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 30, 18, 0), 8, 50);
        String resp = mvc.perform(post("/api/tournaments")
                .contentType("application/json").content(mapper.writeValueAsString(t)))
                .andReturn().getResponse().getContentAsString();
        // Crear via service directamente
        Torneo saved = torneoService.crear(t, new HashMap<>());
        return saved.getId();
    }

    @Test
    void obtenerTorneo_existente_retorna200() throws Exception {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        Torneo saved = torneoService.crear(t, new HashMap<>());
        mvc.perform(get("/api/tournaments/" + saved.getId())).andExpect(status().isOk());
    }

    @Test
    void listarTorneos_retorna200() throws Exception {
        mvc.perform(get("/api/tournaments")).andExpect(status().isOk());
    }

    @Test
    void tablaPosiciones_sinPartidos_retornaListaVacia() throws Exception {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        Torneo saved = torneoService.crear(t, new HashMap<>());
        mvc.perform(get("/api/tournaments/" + saved.getId() + "/positions"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void bracket_sinPartidos_retornaMapVacio() throws Exception {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        Torneo saved = torneoService.crear(t, new HashMap<>());
        mvc.perform(get("/api/tournaments/" + saved.getId() + "/bracket"))
                .andExpect(status().isOk())
                .andExpect(content().string("{}"));
    }

    @Test
    void estadisticas_sinPartidos_retornaMetricas() throws Exception {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        Torneo saved = torneoService.crear(t, new HashMap<>());
        mvc.perform(get("/api/tournaments/" + saved.getId() + "/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPartidos").value(0));
    }

    @Test
    void topScorers_sinPartidos_retornaListaVacia() throws Exception {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        Torneo saved = torneoService.crear(t, new HashMap<>());
        mvc.perform(get("/api/tournaments/" + saved.getId() + "/top-scorers"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void generateBracket_pocosEquipos_retorna409() throws Exception {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        Torneo saved = torneoService.crear(t, new HashMap<>());
        mvc.perform(post("/api/tournaments/" + saved.getId() + "/generate-bracket"))
                .andExpect(status().isConflict());
    }

    @Test
    void inscripcionHabilitada_retorna200() throws Exception {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        Torneo saved = torneoService.crear(t, new HashMap<>());
        mvc.perform(get("/api/tournaments/" + saved.getId() + "/enrollment"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarTorneo_existente_retorna200() throws Exception {
        Torneo t = new Torneo(null, "Copa", LocalDateTime.now(), LocalDateTime.now().plusDays(5), 8, 50);
        Torneo saved = torneoService.crear(t, new HashMap<>());
        mvc.perform(delete("/api/tournaments/" + saved.getId()))
                .andExpect(status().isOk());
    }
}
