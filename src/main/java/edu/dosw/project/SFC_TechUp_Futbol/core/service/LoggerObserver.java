package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggerObserver implements Observer {
    private List<Map<String, Object>> logs;

    public LoggerObserver() {
        this.logs = new ArrayList<>();
    }

    @Override
    public void actualizar(String evento, Map<String, Object> datos) {
        Map<String, Object> log = new HashMap<>();
        log.put("evento", evento);
        log.put("datos", datos);
        this.logs.add(log);
    }

    public List<Map<String, Object>> getLogs() {
        return logs;
    }
}
