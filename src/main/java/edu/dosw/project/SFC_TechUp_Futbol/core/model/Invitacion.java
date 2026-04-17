package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import java.time.LocalDateTime;

public class Invitacion {

    public enum EstadoInvitacion { PENDIENTE, ACEPTADA, RECHAZADA }

    private String id;
    private String jugadorId;
    private String equipoId;
    private Jugador.Posicion posicion;
    private LocalDateTime fecha;
    private EstadoInvitacion estado;

    public Invitacion() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJugadorId() { return jugadorId; }
    public void setJugadorId(String jugadorId) { this.jugadorId = jugadorId; }

    public String getEquipoId() { return equipoId; }
    public void setEquipoId(String equipoId) { this.equipoId = equipoId; }

    public Jugador.Posicion getPosicion() { return posicion; }
    public void setPosicion(Jugador.Posicion posicion) { this.posicion = posicion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoInvitacion getEstado() { return estado; }
    public void setEstado(EstadoInvitacion estado) { this.estado = estado; }
}
