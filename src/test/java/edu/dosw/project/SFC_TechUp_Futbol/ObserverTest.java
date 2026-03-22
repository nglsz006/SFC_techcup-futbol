package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.service.LoggerObserver;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.NotificadorTorneo;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {

    @Test
    void loggerObserver_registraEvento_correctamente() {
        LoggerObserver logger = new LoggerObserver();
        logger.actualizar("TORNEO_CREADO", Map.of("id", 1));
        assertEquals(1, logger.getLogs().size());
        assertEquals("TORNEO_CREADO", logger.getLogs().get(0).get("evento"));
    }

    @Test
    void loggerObserver_variosEventos_acumula() {
        LoggerObserver logger = new LoggerObserver();
        logger.actualizar("EVENTO_1", Map.of());
        logger.actualizar("EVENTO_2", Map.of());
        assertEquals(2, logger.getLogs().size());
    }

    @Test
    void notificadorTorneo_actualizar_noLanzaExcepcion() {
        NotificadorTorneo notificador = new NotificadorTorneo();
        assertDoesNotThrow(() -> notificador.actualizar("PARTIDO_INICIADO", Map.of("id", 5)));
    }
}
