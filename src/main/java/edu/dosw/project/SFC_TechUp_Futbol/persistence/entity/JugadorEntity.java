package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import jakarta.persistence.*;

@Entity
@Table(name = "jugador")
@DiscriminatorValue("JUGADOR")
public class JugadorEntity extends UsuarioEntity {

    @Column(name = "jersey_number")
    private int jerseyNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Jugador.Posicion position;

    @Column(name = "available")
    private boolean available;

    @Column(name = "photo")
    private String photo;

    @Column(name = "equipo_id")
    private Integer equipoId;

    public JugadorEntity() {}

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public Jugador.Posicion getPosition() {
        return position;
    }

    public void setPosition(Jugador.Posicion position) {
        this.position = position;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Integer equipoId) {
        this.equipoId = equipoId;
    }
}
