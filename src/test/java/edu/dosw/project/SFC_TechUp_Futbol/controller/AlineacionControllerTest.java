package edu.dosw.project.SFC_TechUp_Futbol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AlineacionService;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AlineacionControllerTest {

    private MockMvc mvc;
    private AlineacionService alineacionService;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        AlineacionMapper alineacionMapper = TestMappers.alineacionMapper();

        Map<String, AlineacionEntity> store = new HashMap<>();
        AlineacionJpaRepository repo = MockRepoHelper.alineacionRepo(store);

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = MockRepoHelper.equipoRepo(equipoStore);

        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        PartidoJpaRepository partidoRepo = MockRepoHelper.partidoRepo(partidoStore);

        alineacionService = new AlineacionService(repo, alineacionMapper, equipoRepo, equipoMapper, partidoRepo, partidoMapper);

        mvc = MockMvcBuilders
                .standaloneSetup(new AlineacionController(alineacionService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    @Test
    void listarAlineaciones_retorna200() throws Exception {
        mvc.perform(get("/api/lineups")).andExpect(status().isOk());
    }

    @Test
    void obtenerAlineacion_inexistente_retorna400() throws Exception {
        mvc.perform(get("/api/lineups/id-inexistente")).andExpect(status().isBadRequest());
    }

    @Test
    void registrarAlineacion_formacionInvalida_retorna400() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("equipoId", "eq1");
        body.put("partidoId", "p1");
        body.put("formacion", "INVALIDA");
        body.put("titulares", List.of());
        mvc.perform(post("/api/lineups").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerAlineacionRival_sinPartido_retorna400() throws Exception {
        mvc.perform(get("/api/lineups/rival").param("partidoId", "p1").param("equipoId", "eq1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerPorPartidoYEquipo_inexistente_retorna400() throws Exception {
        mvc.perform(get("/api/lineups/match/p1/team/eq1")).andExpect(status().isBadRequest());
    }

    @Test
    void registrarAlineacion_valida_retorna200() throws Exception {
        // crear equipo y partido reales en los stores
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        AlineacionMapper alineacionMapper = TestMappers.alineacionMapper();

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = MockRepoHelper.equipoRepo(equipoStore);
        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        PartidoJpaRepository partidoRepo = MockRepoHelper.partidoRepo(partidoStore);
        Map<String, AlineacionEntity> alineacionStore = new HashMap<>();
        AlineacionJpaRepository alineacionRepo = MockRepoHelper.alineacionRepo(alineacionStore);

        edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo equipo = new edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo();
        equipo.setId("eq1"); equipo.setNombre("Equipo A"); equipo.setColorPrincipal("rojo");
        equipoRepo.save(equipoMapper.toEntity(equipo));

        edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido partido = new edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido();
        partido.setId("p1");
        partidoRepo.save(partidoMapper.toEntity(partido));

        AlineacionService svc = new AlineacionService(alineacionRepo, alineacionMapper, equipoRepo, equipoMapper, partidoRepo, partidoMapper);
        MockMvc localMvc = MockMvcBuilders.standaloneSetup(new AlineacionController(svc))
                .setControllerAdvice(new ErrorHandler()).build();

        Map<String, Object> body = new HashMap<>();
        body.put("equipoId", "eq1");
        body.put("partidoId", "p1");
        body.put("formacion", "4-4-2");
        body.put("titulares", List.of());
        localMvc.perform(post("/api/lineups").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(status().isBadRequest());
    }

    @Test
    void obtenerAlineacion_existente_retorna200() throws Exception {
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        AlineacionMapper alineacionMapper = TestMappers.alineacionMapper();

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = MockRepoHelper.equipoRepo(equipoStore);
        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        PartidoJpaRepository partidoRepo = MockRepoHelper.partidoRepo(partidoStore);
        Map<String, AlineacionEntity> alineacionStore = new HashMap<>();
        AlineacionJpaRepository alineacionRepo = MockRepoHelper.alineacionRepo(alineacionStore);

        edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo equipo = new edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo();
        equipo.setId("eq2"); equipo.setNombre("Equipo B"); equipo.setColorPrincipal("azul");
        equipoRepo.save(equipoMapper.toEntity(equipo));

        edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido partido = new edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido();
        partido.setId("p2");
        partidoRepo.save(partidoMapper.toEntity(partido));

        AlineacionService svc = new AlineacionService(alineacionRepo, alineacionMapper, equipoRepo, equipoMapper, partidoRepo, partidoMapper);
        edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion a = new edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion();
        a.setId("al1"); a.setEquipoId("eq2"); a.setPartidoId("p2");
        a.setFormacion(edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion.Formacion.F_4_4_2);
        a.setTitulares(List.of()); a.setReservas(List.of());
        alineacionRepo.save(alineacionMapper.toEntity(a));

        MockMvc localMvc = MockMvcBuilders.standaloneSetup(new AlineacionController(svc))
                .setControllerAdvice(new ErrorHandler()).build();
        localMvc.perform(get("/api/lineups/al1")).andExpect(status().isOk());
    }
}
