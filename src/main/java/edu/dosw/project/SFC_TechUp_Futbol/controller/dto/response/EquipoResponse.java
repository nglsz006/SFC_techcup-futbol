package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;

import java.util.List;

public class EquipoResponse {

    private final String id;
    private final String nombre;
    private final String escudo;
    private final String colorPrincipal;
    private final String colorSecundario;
    private final String capitanId;
    private final List<String> jugadores;

    public EquipoResponse(Equipo equipo) {
        this.id = equipo.getId();
        this.nombre = equipo.getNombre();
        this.escudo = equipo.getEscudo();
        this.colorPrincipal = equipo.getColorPrincipal();
        this.colorSecundario = equipo.getColorSecundario();
        this.capitanId = equipo.getCapitanId();
        this.jugadores = equipo.getJugadores();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEscudo() { return escudo; }
    public String getColorPrincipal() { return colorPrincipal; }
    public String getColorSecundario() { return colorSecundario; }
    public String getCapitanId() { return capitanId; }
    public List<String> getJugadores() { return jugadores; }
}
