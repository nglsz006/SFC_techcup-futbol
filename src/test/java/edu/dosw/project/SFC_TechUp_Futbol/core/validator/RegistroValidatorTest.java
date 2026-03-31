package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.RegistroValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistroValidatorTest {

    private final RegistroValidator validator = new RegistroValidator();

    @Test
    void correoInstitucional_valido() {
        assertTrue(validator.correoInstitucionalValido("juan@escuelaing.edu.co"));
    }

    @Test
    void correoInstitucional_invalido() {
        assertFalse(validator.correoInstitucionalValido("juan@gmail.com"));
        assertFalse(validator.correoInstitucionalValido(null));
    }

    @Test
    void correoGmail_valido() {
        assertTrue(validator.correoGmailValido("juan@gmail.com"));
    }

    @Test
    void correoGmail_invalido() {
        assertFalse(validator.correoGmailValido("juan@escuelaing.edu.co"));
        assertFalse(validator.correoGmailValido(null));
    }

    @Test
    void correoSegunTipo_familiar_usaGmail() {
        assertTrue(validator.correoValidoSegunTipo("juan@gmail.com", Usuario.TipoUsuario.FAMILIAR));
        assertFalse(validator.correoValidoSegunTipo("juan@escuelaing.edu.co", Usuario.TipoUsuario.FAMILIAR));
    }

    @Test
    void correoSegunTipo_estudiante_usaInstitucional() {
        assertTrue(validator.correoValidoSegunTipo("juan@escuelaing.edu.co", Usuario.TipoUsuario.ESTUDIANTE));
        assertFalse(validator.correoValidoSegunTipo("juan@gmail.com", Usuario.TipoUsuario.ESTUDIANTE));
    }

    @Test
    void tipoUsuario_valido_noNulo() {
        assertTrue(validator.tipoUsuarioValido(Usuario.TipoUsuario.ESTUDIANTE));
        assertFalse(validator.tipoUsuarioValido(null));
    }
}
