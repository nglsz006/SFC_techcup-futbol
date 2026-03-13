package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.service.TorneoService;
import edu.dosw.project.SFC_TechUp_Futbol.service.ObserverPattern;
import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TorneoController {
    private TorneoService service;

    public TorneoController() {
        this.service = new TorneoService();
        this.service.agregarObserver(new ObserverPattern.NotificadorTorneo());
        this.service.agregarObserver(new ObserverPattern.LoggerObserver());
    }

    public Torneo crearTorneo(Map<String, Object> datos) {
        Torneo torneo = new Torneo();
        torneo.setNombre((String) datos.get("nombre"));
        torneo.setFechaInicio((LocalDateTime) datos.get("fechaInicio"));
        torneo.setFechaFin((LocalDateTime) datos.get("fechaFin"));
        torneo.setCantidadEquipos((Integer) datos.get("cantidadEquipos"));
        torneo.setCosto((Double) datos.get("costo"));
        return service.crear(torneo, datos);
    }

    public Torneo obtenerTorneo(int id) {
        return service.obtener(id);
    }

    public List<Torneo> listarTorneos() {
        return service.listar();
    }

    public Torneo iniciarTorneo(int id) {
        return service.iniciar(id);
    }
}
