package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "arbitro")
@DiscriminatorValue("ARBITRO")
public class ArbitroEntity extends UsuarioEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "arbitro_partido",
        joinColumns = @JoinColumn(name = "arbitro_id"),
        inverseJoinColumns = @JoinColumn(name = "partido_id")
    )
    private List<PartidoEntity> assignedMatches = new ArrayList<>();

    public ArbitroEntity() {}

    public List<PartidoEntity> getAssignedMatches() {
        return assignedMatches;
    }

    public void setAssignedMatches(List<PartidoEntity> assignedMatches) {
        this.assignedMatches = assignedMatches;
    }
}
