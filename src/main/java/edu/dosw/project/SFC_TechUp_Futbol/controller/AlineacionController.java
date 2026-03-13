package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.service.AlineacionService;
import edu.dosw.project.SFC_TechUp_Futbol.service.ObserverPattern;
import edu.dosw.project.SFC_TechUp_Futbol.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.model.Formacion;

import java.util.List;
import java.util.Map;

public class AlineacionController {
    private AlineacionService service;

    public AlineacionController() {
        this.service = new AlineacionService();
        this.service.agregarObserver(new ObserverPattern.NotificadorTorneo());
        this.service.agregarObserver(new ObserverPattern.LoggerObserver());
    }

    public Alineacion crearAlineacion(Map<String, Object> datos) {
        Alineacion alineacion = new Alineacion();
        alineacion.setEquipoId((Integer) datos.get("equipoId"));
        alineacion.setPartidoId((Integer) datos.get("partidoId"));
        
        String formacionStr = (String) datos.get("formacion");
        alineacion.setFormacion(Formacion.fromString(formacionStr));
        
        alineacion.setTitulares((List<Integer>) datos.get("titulares"));
        alineacion.setReservas((List<Integer>) datos.getOrDefault("reservas", List.of()));
        return service.crear(alineacion, datos);
    }

    public Alineacion obtenerAlineacion(int id) {
        return service.obtener(id);
    }
}
