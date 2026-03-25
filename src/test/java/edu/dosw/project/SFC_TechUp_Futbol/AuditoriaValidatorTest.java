package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.ConsultaAuditoriaRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.FiltroAuditoriaInvalidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AuditoriaValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuditoriaValidatorTest {

    private final AuditoriaValidator validator = new AuditoriaValidator();

    @Test
    void validarConsulta_conFiltrosValidos_noLanzaExcepcion() {
        ConsultaAuditoriaRequest request = new ConsultaAuditoriaRequest();
        request.setUsuario("admin");
        request.setTipoAccion("REGISTRO_ORGANIZADOR");
        request.setFechaDesde(LocalDate.now().minusDays(3));
        request.setFechaHasta(LocalDate.now());

        assertDoesNotThrow(() -> validator.validarConsulta(request));
    }

    @Test
    void validarConsulta_conTipoAccionInvalido_lanzaExcepcion() {
        ConsultaAuditoriaRequest request = new ConsultaAuditoriaRequest();
        request.setTipoAccion("BORRADO_TOTAL");

        assertThrows(FiltroAuditoriaInvalidoException.class, () -> validator.validarConsulta(request));
    }

    @Test
    void validarConsulta_conRangoInconsistente_lanzaExcepcion() {
        ConsultaAuditoriaRequest request = new ConsultaAuditoriaRequest();
        request.setFechaDesde(LocalDate.now());
        request.setFechaHasta(LocalDate.now().minusDays(1));

        assertThrows(FiltroAuditoriaInvalidoException.class, () -> validator.validarConsulta(request));
    }
}
