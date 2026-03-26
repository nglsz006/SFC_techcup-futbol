package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("JUGADOR")
public class Jugador extends Usuario {

    public enum Posicion { PORTERO, DEFENSA, VOLANTE, DELANTERO }

    @Column(name = "jersey_number")
    private int jerseyNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "posicion")
    private Posicion position;

    @Column(name = "available")
    private boolean available;

    @Column(name = "photo")
    private String photo;

    @Column(name = "equipo_id")
    private Integer equipoId;

    public Jugador() {}

    public Jugador(Long id, String name, String email, String password, TipoUsuario userType,
                   int jerseyNumber, Posicion position, boolean available, String photo) {
        super(id, name, email, password, userType);
        this.jerseyNumber = jerseyNumber;
        this.position = position;
        this.available = available;
        this.photo = photo;
    }

    public int getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(int jerseyNumber) { this.jerseyNumber = jerseyNumber; }

    public Posicion getPosition() { return position; }
    public void setPosition(Posicion position) { this.position = position; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public Integer getEquipo() { return equipoId; }
    public void setEquipo(Integer equipoId) { this.equipoId = equipoId; }
}
