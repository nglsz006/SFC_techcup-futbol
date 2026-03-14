package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Gol {

    private Long id;
    private int minuto;
    private Jugador jugador;
    @JsonIgnore
    private Partido partido;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getMinuto() { return minuto; }
    public void setMinuto(int minuto) { this.minuto = minuto; }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    public Partido getPartido() { return partido; }
    public void setPartido(Partido partido) { this.partido = partido; }
}

