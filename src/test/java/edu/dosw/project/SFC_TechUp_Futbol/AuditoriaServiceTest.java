package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.ConsultaAuditoriaRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.RegistroAuditoriaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AuditoriaServiceTest {

    private AuditoriaService auditoriaService;

    @BeforeEach
    void setUp() {
        List<RegistroAuditoria> store = new ArrayList<>();
        AtomicLong idGen = new AtomicLong(1);
        RegistroAuditoriaRepository repo = mock(RegistroAuditoriaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> {
            RegistroAuditoria r = inv.getArgument(0);
            if (r.getId() == null) r.setId(idGen.getAndIncrement());
            store.add(r);
            return r;
        });
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store));
        auditoriaService = new AuditoriaService(repo);
    }

    @Test
    void consultarHistorial_conFiltros_validos_retornaOrdenadoDeMasRecienteAMasAntiguo() throws InterruptedException {
        auditoriaService.registrarEvento(1L, "org1@escuelaing.edu.co", TipoAccionAuditoria.REGISTRO_ORGANIZADOR, "Registro 1");
        Thread.sleep(5);
        auditoriaService.registrarEvento(1L, "arb1@escuelaing.edu.co", TipoAccionAuditoria.REGISTRO_ARBITRO, "Registro 2");

        ConsultaAuditoriaRequest request = new ConsultaAuditoriaRequest();
        request.setFechaDesde(LocalDate.now().minusDays(1));
        request.setFechaHasta(LocalDate.now().plusDays(1));

        List<?> registros = auditoriaService.consultarHistorial(request);

        assertEquals(2, registros.size());
        assertEquals("arb1@escuelaing.edu.co", auditoriaService.consultarHistorial(request).getFirst().getUsuario());
    }

    @Test
    void consultarHistorial_cuandoNoHayCoincidencias_retornaListaVacia() {
        auditoriaService.registrarEvento(1L, "org1@escuelaing.edu.co", TipoAccionAuditoria.REGISTRO_ORGANIZADOR, "Registro 1");

        ConsultaAuditoriaRequest request = new ConsultaAuditoriaRequest();
        request.setUsuario("sin-coincidencias");

        assertTrue(auditoriaService.consultarHistorial(request).isEmpty());
    }
}
