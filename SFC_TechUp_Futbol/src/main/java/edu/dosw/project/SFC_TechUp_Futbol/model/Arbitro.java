package edu.dosw.project.SFC_TechUp_Futbol.model;

import java.util.ArrayList;
import java.util.List;

public class Arbitro extends Usuario {

    private List<Partido> assignedMatches;

    public Arbitro() {
        this.assignedMatches = new ArrayList<>();
    }

    public Arbitro(Long id, String name, String email,
                   String password, TipoUsuario userType) {
        super(id, name, email, password, userType);
        this.assignedMatches = new ArrayList<>();
    }

    public List<Partido> getAssignedMatches() {
        return assignedMatches;
    }

    public void setAssignedMatches(List<Partido> assignedMatches) {
        this.assignedMatches = assignedMatches;
    }
}