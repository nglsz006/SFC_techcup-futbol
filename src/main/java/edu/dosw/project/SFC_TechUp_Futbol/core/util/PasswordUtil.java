package edu.dosw.project.SFC_TechUp_Futbol.core.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String cifrar(String password) {
        return encoder.encode(password);
    }

    public static boolean verificar(String password, String hash) {
        return encoder.matches(password, hash);
    }
}
