package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private int id;
    private String nombre;
    private String escudo;
    private String colorPrincipal;
    private String colorSecundario;
    private int capitanId;
    private List<Integer> jugadores;

    public Equipo() {
        this.jugadores = new ArrayList<>();
    }

    public Equipo(int id, String nombre, String escudo, String colorPrincipal, 
                  String colorSecundario, int capitanId) {
        this.id = id;
        this.nombre = nombre;
        this.escudo = escudo;
        this.colorPrincipal = colorPrincipal;
        this.colorSecundario = colorSecundario;
        this.capitanId = capitanId;
        this.jugadores = new ArrayList<>();
    }

    public void agregarJugador(int jugadorId) {
        this.jugadores.add(jugadorId);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }

    public String getColorPrincipal() { return colorPrincipal; }
    public void setColorPrincipal(String colorPrincipal) { this.colorPrincipal = colorPrincipal; }

    public String getColorSecundario() { return colorSecundario; }
    public void setColorSecundario(String colorSecundario) { this.colorSecundario = colorSecundario; }

    public int getCapitanId() { return capitanId; }
    public void setCapitanId(int capitanId) { this.capitanId = capitanId; }

    public List<Integer> getJugadores() { return jugadores; }
    public void setJugadores(List<Integer> jugadores) { this.jugadores = jugadores; }
}

