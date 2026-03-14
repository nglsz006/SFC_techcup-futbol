package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Tarjeta {

    public enum TipoTarjeta {
        AMARILLA, ROJA
    }

    private Long id;
    private TipoTarjeta tipo;
    private int minuto;
    private Jugador jugador;
    @JsonIgnore
    private Partido partido;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoTarjeta getTipo() { return tipo; }
    public void setTipo(TipoTarjeta tipo) { this.tipo = tipo; }

    public int getMinuto() { return minuto; }
    public void setMinuto(int minuto) { this.minuto = minuto; }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    public Partido getPartido() { return partido; }
    public void setPartido(Partido partido) { this.partido = partido; }
}

