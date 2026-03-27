package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ARBITRO")
public class Arbitro extends Usuario {

    @ManyToMany
    @JoinTable(
        name = "arbitro_partidos",
        joinColumns = @JoinColumn(name = "arbitro_id"),
        inverseJoinColumns = @JoinColumn(name = "partido_id")
    )
    private List<Partido> assignedMatches = new ArrayList<>();

    public Arbitro() {}

    public Arbitro(Long id, String name, String email, String password, TipoUsuario userType) {
        super(id, name, email, password, userType);
    }

    public List<Partido> getAssignedMatches() { return assignedMatches; }
    public void setAssignedMatches(List<Partido> assignedMatches) { this.assignedMatches = assignedMatches; }
}
