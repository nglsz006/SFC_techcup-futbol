package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Invitacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invitacion")
public class InvitacionEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "jugador_id", nullable = false)
    private String jugadorId;

    @Column(name = "equipo_id", nullable = false)
    private String equipoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "posicion")
    private Jugador.Posicion posicion;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Invitacion.EstadoInvitacion estado;

    public InvitacionEntity() {}

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

    public Invitacion.EstadoInvitacion getEstado() { return estado; }
    public void setEstado(Invitacion.EstadoInvitacion estado) { this.estado = estado; }
}
