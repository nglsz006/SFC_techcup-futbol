package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AccesoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.dto.request.RegistroRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccesoValidatorTest {

    private final AccesoValidator validator = new AccesoValidator();

    private RegistroRequest registroValido() {
        RegistroRequest req = new RegistroRequest();
        req.setNombre("Juan");
        req.setEmail("juan@escuelaing.edu.co");
        req.setPassword("12345678");
        req.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        return req;
    }

    private LoginRequest loginValido() {
        LoginRequest req = new LoginRequest();
        req.setEmail("juan@escuelaing.edu.co");
        req.setPassword("12345678");
        return req;
    }

    @Test
    void validarLogin_correcto_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validator.validarLogin(loginValido()));
    }

    @Test
    void validarLogin_sinEmail_lanzaExcepcion() {
        LoginRequest req = loginValido();
        req.setEmail(null);
        assertThrows(IllegalArgumentException.class, () -> validator.validarLogin(req));
    }

    @Test
    void validarLogin_sinPassword_lanzaExcepcion() {
        LoginRequest req = loginValido();
        req.setPassword(null);
        assertThrows(IllegalArgumentException.class, () -> validator.validarLogin(req));
    }

    @Test
    void validarRegistro_correcto_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validator.validarRegistro(registroValido()));
    }

    @Test
    void validarRegistro_familiar_correoGmail_noLanzaExcepcion() {
        RegistroRequest req = registroValido();
        req.setEmail("familiar@gmail.com");
        req.setTipoUsuario(Usuario.TipoUsuario.FAMILIAR);
        assertDoesNotThrow(() -> validator.validarRegistro(req));
    }

    @Test
    void validarRegistro_familiar_correoInstitucional_lanzaExcepcion() {
        RegistroRequest req = registroValido();
        req.setTipoUsuario(Usuario.TipoUsuario.FAMILIAR);
        assertThrows(IllegalArgumentException.class, () -> validator.validarRegistro(req));
    }

    @Test
    void validarRegistro_estudiante_correoGmail_lanzaExcepcion() {
        RegistroRequest req = registroValido();
        req.setEmail("juan@gmail.com");
        assertThrows(IllegalArgumentException.class, () -> validator.validarRegistro(req));
    }

    @Test
    void validarRegistro_passwordCorta_lanzaExcepcion() {
        RegistroRequest req = registroValido();
        req.setPassword("123");
        assertThrows(IllegalArgumentException.class, () -> validator.validarRegistro(req));
    }

    @Test
    void validarRegistro_sinNombre_lanzaExcepcion() {
        RegistroRequest req = registroValido();
        req.setNombre("");
        assertThrows(IllegalArgumentException.class, () -> validator.validarRegistro(req));
    }
}
