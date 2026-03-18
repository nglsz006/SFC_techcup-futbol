package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import java.util.Map;

public interface Observer {
    void actualizar(String evento, Map<String, Object> datos);
}
