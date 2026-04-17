package edu.dosw.project.SFC_TechUp_Futbol.core.util;

import java.util.Base64;

public class Base64Util {

    private Base64Util() {}

    public static String encode(String value) {
        if (value == null) return null;
        return Base64.getEncoder().encodeToString(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public static String decode(String value) {
        if (value == null) return null;
        try {
            return new String(Base64.getDecoder().decode(value), java.nio.charset.StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return value;
        }
    }
}
