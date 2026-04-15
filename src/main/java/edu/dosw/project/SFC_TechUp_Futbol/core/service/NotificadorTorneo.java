package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import java.util.Map;
import java.util.logging.Logger;

public class NotificadorTorneo implements Observer {

    private static final Logger log = Logger.getLogger(NotificadorTorneo.class.getName());

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    @Override
    public void actualizar(String evento, Map<String, Object> datos) {
        log.info("[notificacion] " + sanitize(evento) + ": " + sanitize(datos.toString()));
    }
}

