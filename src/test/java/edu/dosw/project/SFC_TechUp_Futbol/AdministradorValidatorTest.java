package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdministradorValidatorTest {

    private final AdministradorValidator administradorValidator = new AdministradorValidator();

    @Test
    void validarAdministradorId_cuandoEsValido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> administradorValidator.validarAdministradorId(1L));
    }

    @Test
    void validarAdministradorId_cuandoEsNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> administradorValidator.validarAdministradorId(null));
    }

    @Test
    void validarRegistro_cuandoCorreoNoCorrespondeAlTipo_lanzaExcepcion() {
        RegistroAdministrativoRequest request = new RegistroAdministrativoRequest();
        request.setNombre("Familiar");
        request.setEmail("familiar@escuelaing.edu.co");
        request.setPassword("password123");
        request.setTipoUsuario(Usuario.TipoUsuario.FAMILIAR);

        assertThrows(IllegalArgumentException.class, () -> administradorValidator.validarRegistro(request));
    }

    @Test
    void validarRegistro_cuandoEsValido_noLanzaExcepcion() {
        RegistroAdministrativoRequest request = new RegistroAdministrativoRequest();
        request.setNombre("Organizador");
        request.setEmail("organizador@escuelaing.edu.co");
        request.setPassword("password123");
        request.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);

        assertDoesNotThrow(() -> administradorValidator.validarRegistro(request));
    }
}
