package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ORGANIZADOR")
public class Organizador extends Usuario {

    @ManyToOne
    @JoinColumn(name = "torneo_id")
    private Torneo currentTournament;

    public Organizador() {}

    public Organizador(Long id, String name, String email, String password,
                       TipoUsuario userType, Torneo currentTournament) {
        super(id, name, email, password, userType);
        this.currentTournament = currentTournament;
    }

    public Torneo getCurrentTournament() { return currentTournament; }
    public void setCurrentTournament(Torneo currentTournament) { this.currentTournament = currentTournament; }
}
