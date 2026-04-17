package edu.dosw.project.SFC_TechUp_Futbol.core.util;

import java.util.UUID;

public class IdGeneratorUtil {

    public static String generarId() {
        return UUID.randomUUID().toString();
    }

}
