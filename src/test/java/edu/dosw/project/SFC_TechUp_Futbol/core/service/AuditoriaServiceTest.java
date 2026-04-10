package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.ConsultaAuditoriaRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.RegistroAuditoriaEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.RegistroAuditoriaMapper;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.RegistroAuditoriaJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AuditoriaServiceTest {

    private AuditoriaService auditoriaService;

    @BeforeEach
    void setUp() {
        List<RegistroAuditoriaEntity> store = new ArrayList<>();
        RegistroAuditoriaJpaRepository repo = mock(RegistroAuditoriaJpaRepository.class);
        RegistroAuditoriaMapper mapper = TestMappers.registroAuditoriaMapper();
        when(repo.save(any())).thenAnswer(inv -> {
            RegistroAuditoriaEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            store.add(e);
            return e;
        });
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store));
        auditoriaService = new AuditoriaService(repo, mapper);
    }

    @Test
    void consultarHistorial_conFiltros_validos_retornaOrdenadoDeMasRecienteAMasAntiguo() throws InterruptedException {
        auditoriaService.registrarEvento("uuid-admin-1", "org1@escuelaing.edu.co", TipoAccionAuditoria.REGISTRO_ORGANIZADOR, "Registro 1");
        Thread.sleep(5);
        auditoriaService.registrarEvento("uuid-admin-1", "arb1@escuelaing.edu.co", TipoAccionAuditoria.REGISTRO_ARBITRO, "Registro 2");

        ConsultaAuditoriaRequest request = new ConsultaAuditoriaRequest();
        request.setFechaDesde(LocalDate.now().minusDays(1));
        request.setFechaHasta(LocalDate.now().plusDays(1));

        List<?> registros = auditoriaService.consultarHistorial(request);

        assertEquals(2, registros.size());
        assertEquals("arb1@escuelaing.edu.co", auditoriaService.consultarHistorial(request).getFirst().getUsuario());
    }

    @Test
    void consultarHistorial_cuandoNoHayCoincidencias_retornaListaVacia() {
        auditoriaService.registrarEvento("uuid-admin-1", "org1@escuelaing.edu.co", TipoAccionAuditoria.REGISTRO_ORGANIZADOR, "Registro 1");

        ConsultaAuditoriaRequest request = new ConsultaAuditoriaRequest();
        request.setUsuario("sin-coincidencias");

        assertTrue(auditoriaService.consultarHistorial(request).isEmpty());
    }
}
