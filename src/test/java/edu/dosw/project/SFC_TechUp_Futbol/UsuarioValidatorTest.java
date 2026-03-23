package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.validator.UsuarioValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioValidatorTest {

    private final UsuarioValidator validator = new UsuarioValidator();

    @Test
    void correoValido_conArrobaYPunto() {
        assertTrue(validator.correoValido("juan@correo.com"));
    }

    @Test
    void correoInvalido_sinArroba() {
        assertFalse(validator.correoValido("juancorreo.com"));
    }

    @Test
    void correoInvalido_sinPunto() {
        assertFalse(validator.correoValido("juan@correocom"));
    }

    @Test
    void correoInvalido_nulo() {
        assertFalse(validator.correoValido(null));
    }

    @Test
    void contrasenaValida_ochoCaracteres() {
        assertTrue(validator.contrasenaValida("12345678"));
    }

    @Test
    void contrasenaInvalida_menosDeOcho() {
        assertFalse(validator.contrasenaValida("1234"));
    }

    @Test
    void contrasenaInvalida_nula() {
        assertFalse(validator.contrasenaValida(null));
    }

    @Test
    void nombreValido_conTexto() {
        assertTrue(validator.nombreValido("Juan"));
    }

    @Test
    void nombreInvalido_vacio() {
        assertFalse(validator.nombreValido("   "));
    }

    @Test
    void nombreInvalido_nulo() {
        assertFalse(validator.nombreValido(null));
    }
}
