package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.service.EquipoService;
import edu.dosw.project.SFC_TechUp_Futbol.service.NotificadorTorneo;
import edu.dosw.project.SFC_TechUp_Futbol.service.LoggerObserver;
import edu.dosw.project.SFC_TechUp_Futbol.model.Equipo;

import java.util.List;
import java.util.Map;

public class EquipoController {
    private EquipoService service;

    public EquipoController() {
        this.service = new EquipoService();
        this.service.agregarObserver(new NotificadorTorneo());
        this.service.agregarObserver(new LoggerObserver());
    }

    public Equipo crearEquipo(Map<String, Object> datos) {
        Equipo equipo = new Equipo();
        equipo.setNombre((String) datos.get("nombre"));
        equipo.setEscudo((String) datos.getOrDefault("escudo", ""));
        equipo.setColorPrincipal((String) datos.get("colorPrincipal"));
        equipo.setColorSecundario((String) datos.getOrDefault("colorSecundario", ""));
        equipo.setCapitanId((Integer) datos.get("capitanId"));
        return service.crear(equipo, datos);
    }

    public Equipo obtenerEquipo(int id) {
        return service.obtener(id);
    }

    public List<Equipo> listarEquipos() {
        return service.listar();
    }

    public Equipo agregarJugador(int equipoId, int jugadorId) {
        return service.agregarJugador(equipoId, jugadorId);
    }
}
