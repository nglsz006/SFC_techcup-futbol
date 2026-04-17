package edu.dosw.project.SFC_TechUp_Futbol.core.util;

import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void cifrar_retornaHashDistintoAlOriginal() {
        String hash = PasswordUtil.cifrar("12345678");
        assertNotEquals("12345678", hash);
    }

    @Test
    void cifrar_mismaPassword_generaHashesDiferentes() {
        String hash1 = PasswordUtil.cifrar("12345678");
        String hash2 = PasswordUtil.cifrar("12345678");
        assertNotEquals(hash1, hash2);
    }

    @Test
    void verificar_passwordCorrecta_retornaTrue() {
        String hash = PasswordUtil.cifrar("12345678");
        assertTrue(PasswordUtil.verificar("12345678", hash));
    }

    @Test
    void verificar_passwordIncorrecta_retornaFalse() {
        String hash = PasswordUtil.cifrar("12345678");
        assertFalse(PasswordUtil.verificar("wrongpassword", hash));
    }
}
