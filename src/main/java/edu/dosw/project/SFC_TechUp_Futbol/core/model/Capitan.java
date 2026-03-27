package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CAPITAN")
public class Capitan extends Jugador {

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Equipo team;

    public Capitan() {}

    public Capitan(Long id, String name, String email, String password, TipoUsuario userType,
                   int jerseyNumber, Posicion position, boolean available, String photo, Equipo team) {
        super(id, name, email, password, userType, jerseyNumber, position, available, photo);
        this.team = team;
    }

    public Equipo getTeam() { return team; }
    public void setTeam(Equipo team) { this.team = team; }
}
