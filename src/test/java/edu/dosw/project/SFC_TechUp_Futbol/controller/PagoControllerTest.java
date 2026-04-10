package edu.dosw.project.SFC_TechUp_Futbol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PagoControllerTest {

    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        PagoMapper pagoMapper = TestMappers.pagoMapper(equipoMapper);

        Map<String, EquipoEntity> equipoStore = new HashMap<>();
        EquipoJpaRepository equipoRepo = MockRepoHelper.equipoRepo(equipoStore);

        Map<String, PagoEntity> pagoStore = new HashMap<>();
        PagoJpaRepository pagoRepo = MockRepoHelper.pagoRepo(pagoStore);

        PagoService pagoService = new edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoServiceImpl(pagoRepo, pagoMapper, equipoRepo, equipoMapper);

        mvc = MockMvcBuilders
                .standaloneSetup(new PagoController(pagoService, new PagoValidator()))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    @Test
    void consultarPago_inexistente_retorna404() throws Exception {
        mvc.perform(get("/api/payments/id-inexistente")).andExpect(status().isNotFound());
    }

    @Test
    void consultarEstadoPago_inexistente_retorna404() throws Exception {
        mvc.perform(get("/api/payments/id-inexistente/status")).andExpect(status().isNotFound());
    }

    @Test
    void consultarPagosPorEquipo_sinPagos_retorna200() throws Exception {
        mvc.perform(get("/api/payments/team/eq1")).andExpect(status().isOk());
    }

    @Test
    void consultarPagosPorEstado_valido_retorna200() throws Exception {
        mvc.perform(get("/api/payments/status/PENDIENTE")).andExpect(status().isOk());
    }

    @Test
    void consultarPagosPorEstado_invalido_retorna400() throws Exception {
        mvc.perform(get("/api/payments/status/INVALIDO")).andExpect(status().isBadRequest());
    }

    @Test
    void equipoHabilitado_retorna200() throws Exception {
        mvc.perform(get("/api/payments/team/eq1/enabled")).andExpect(status().isOk());
    }

    @Test
    void enviarARevision_inexistente_retorna404() throws Exception {
        mvc.perform(patch("/api/payments/id-inexistente/review")).andExpect(status().isNotFound());
    }
}
