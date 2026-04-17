package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Organizador extends Usuario {

    @JsonIgnore
    private Torneo currentTournament;

    public Organizador() {}

    public Organizador(String id, String name, String email, String password, TipoUsuario userType, Torneo currentTournament) {
        super(id, name, email, password, userType);
        this.currentTournament = currentTournament;
    }

    public Torneo getCurrentTournament() {
        return currentTournament;
    }

    public void setCurrentTournament(Torneo currentTournament) {
        this.currentTournament = currentTournament;
    }
}

