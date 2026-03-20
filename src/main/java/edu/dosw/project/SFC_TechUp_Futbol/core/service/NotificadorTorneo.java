package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import java.util.Map;

public class NotificadorTorneo implements Observer {
    @Override
    public void actualizar(String evento, Map<String, Object> datos) {
        System.out.println("[notificacion] " + evento + ": " + datos);
    }
}
