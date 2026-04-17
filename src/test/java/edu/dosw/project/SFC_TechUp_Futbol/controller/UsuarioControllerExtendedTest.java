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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerExtendedTest {

    private MockMvc mvc;
    private JugadorService jugadorService;
    private CapitanService capitanService;
    private PerfilDeportivoService perfilService;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        PerfilDeportivoMapper perfilMapper = TestMappers.perfilDeportivoMapper();

        Map<String, TorneoEntity> torneoStore = new HashMap<>();
        TorneoJpaRepository torneoRepo = MockRepoHelper.torneoRepo(torneoStore);

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = MockRepoHelper.equipoRepo(equipoStore);
        EquipoService equipoService = new EquipoService(equipoRepo, equipoMapper);

        Map<String, JugadorEntity> jugadorStore = new HashMap<>();
        JugadorJpaRepository jugadorRepo = MockRepoHelper.jugadorRepo(jugadorStore);
        jugadorService = new JugadorService(jugadorRepo, jugadorMapper);

        Map<String, PartidoEntity> partidoStore = new HashMap<>();
        PartidoJpaRepository partidoRepo = MockRepoHelper.partidoRepo(partidoStore);
        PartidoServiceImpl partidoService = new PartidoServiceImpl(partidoRepo, partidoMapper, torneoRepo, torneoMapper, equipoRepo, equipoMapper, jugadorRepo, jugadorMapper);

        Map<String, ArbitroEntity> arbitroStore = new HashMap<>();
        ArbitroJpaRepository arbitroRepo = MockRepoHelper.arbitroRepo(arbitroStore);
        ArbitroMapper arbitroMapper = TestMappers.arbitroMapper(partidoMapper);
        ArbitroService arbitroService = new ArbitroService(arbitroRepo, arbitroMapper);

        Map<String, CapitanEntity> capitanStore = new HashMap<>();
        CapitanJpaRepository capitanRepo = MockRepoHelper.capitanRepo(capitanStore);
        CapitanMapper capitanMapper = TestMappers.capitanMapper(equipoRepo, equipoMapper);
        capitanService = new CapitanService(capitanRepo, capitanMapper, jugadorService, jugadorRepo, jugadorMapper);

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        OrganizadorMapper orgMapper = TestMappers.organizadorMapper(torneoRepo, torneoMapper);
        OrganizadorJpaRepository orgRepo = MockRepoHelper.orgRepo(orgStore);
        OrganizadorService organizadorService = new OrganizadorService(orgRepo, orgMapper, new TorneoService(torneoRepo, torneoMapper));

        Map<String, PagoEntity> pagoStore = new HashMap<>();
        PagoJpaRepository pagoRepo = MockRepoHelper.pagoRepo(pagoStore);
        PagoMapper pagoMapper = TestMappers.pagoMapper(equipoMapper);
        PagoServiceImpl pagoService = new PagoServiceImpl(pagoRepo, pagoMapper, equipoRepo, equipoMapper);

        Map<String, PerfilDeportivoEntity> perfilStore = new HashMap<>();
        PerfilDeportivoJpaRepository perfilRepo = MockRepoHelper.perfilRepo(perfilStore);
        perfilService = new PerfilDeportivoServiceImpl(perfilRepo, perfilMapper, jugadorRepo);

        mvc = MockMvcBuilders
                .standaloneSetup(new UsuarioController(jugadorService, capitanService, arbitroService,
                        organizadorService, pagoService, partidoService, new PartidoValidator(),
                        equipoService, new TorneoService(torneoRepo, torneoMapper), perfilService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    private String crearJugadorYObtenerId(String email) throws Exception {
        Map<String, Object> body = Map.of("nombre", "Juan", "email", email, "password", "pass",
                "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 10, "posicion", "DELANTERO");
        mvc.perform(post("/api/users/players").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(status().isOk());
        return jugadorService.getJugadores().stream()
                .reduce((a, b) -> b).get().getId();
    }

    @Test
    void crearPerfilDeportivo_valido_retorna200() throws Exception {
        String jugId = crearJugadorYObtenerId("perfil@test.com");
        Map<String, Object> body = new HashMap<>();
        body.put("posiciones", List.of("DELANTERO"));
        body.put("dorsal", 10);
        body.put("foto", "");
        body.put("edad", 22);
        body.put("genero", "MASCULINO");
        body.put("identificacion", "123456");
        mvc.perform(post("/api/users/players/" + jugId + "/profile")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void consultarPerfilDeportivo_existente_retorna200() throws Exception {
        String jugId = crearJugadorYObtenerId("perfil2@test.com");
        Map<String, Object> body = new HashMap<>();
        body.put("posiciones", List.of("PORTERO"));
        body.put("dorsal", 1);
        body.put("foto", "");
        body.put("edad", 25);
        body.put("genero", "FEMENINO");
        body.put("identificacion", "654321");
        mvc.perform(post("/api/users/players/" + jugId + "/profile")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)));
        mvc.perform(get("/api/users/players/" + jugId + "/profile"))
                .andExpect(status().isOk());
    }

    @Test
    void consultarPerfilDeportivo_inexistente_retorna400() throws Exception {
        String jugId = crearJugadorYObtenerId("perfil3@test.com");
        mvc.perform(get("/api/users/players/" + jugId + "/profile"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarJugadoresAvanzado_sinFiltros_retorna200() throws Exception {
        crearCapitan("capavanzado@test.com");
        mvc.perform(get("/api/users/captains/capavanzado@test.com/search-players/advanced"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarJugadoresAvanzado_conPosicion_retorna200() throws Exception {
        crearCapitan("capavanzado2@test.com");
        mvc.perform(get("/api/users/captains/capavanzado2@test.com/search-players/advanced")
                .param("posicion", "DELANTERO"))
                .andExpect(status().isOk());
    }

    @Test
    void toggleRol_jugadorACapitan_retorna200() throws Exception {
        crearJugadorYObtenerId("toggle@test.com");
        mvc.perform(patch("/api/users/players/toggle@test.com/profile/toggle-role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Rol cambiado a CAPITAN"));
    }

    @Test
    void toggleRol_idInexistente_retorna400() throws Exception {
        mvc.perform(patch("/api/users/players/inexistente/profile/toggle-role"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarJugador_sinEquipo_retorna200() throws Exception {
        String jugId = crearJugadorYObtenerId("eliminar@test.com");
        mvc.perform(delete("/api/users/players/" + jugId))
                .andExpect(status().isOk());
    }

    private String crearCapitan(String email) throws Exception {
        Map<String, Object> body = Map.of("nombre", "Cap", "email", email, "password", "pass",
                "tipoUsuario", "ESTUDIANTE", "numeroCamiseta", 1, "posicion", "PORTERO");
        return mapper.readTree(mvc.perform(post("/api/users/captains")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString()).get("id").asText();
    }
}
