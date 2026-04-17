package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private String id;
    private String nombre;
    private String escudo;
    private String colorPrincipal;
    private String colorSecundario;
    private String capitanId;
    private List<String> jugadores;

    public Equipo() {
        this.jugadores = new ArrayList<>();
    }

    public Equipo(String id, String nombre, String escudo, String colorPrincipal,
                  String colorSecundario, String capitanId) {
        this.id = id;
        this.nombre = nombre;
        this.escudo = escudo;
        this.colorPrincipal = colorPrincipal;
        this.colorSecundario = colorSecundario;
        this.capitanId = capitanId;
        this.jugadores = new ArrayList<>();
    }

    public void agregarJugador(String jugadorId) {
        this.jugadores.add(jugadorId);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }

    public String getColorPrincipal() { return colorPrincipal; }
    public void setColorPrincipal(String colorPrincipal) { this.colorPrincipal = colorPrincipal; }

    public String getColorSecundario() { return colorSecundario; }
    public void setColorSecundario(String colorSecundario) { this.colorSecundario = colorSecundario; }

    public String getCapitanId() { return capitanId; }
    public void setCapitanId(String capitanId) { this.capitanId = capitanId; }

    public List<String> getJugadores() { return jugadores; }
    public void setJugadores(List<String> jugadores) { this.jugadores = jugadores; }
}

