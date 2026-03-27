package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "capitan")
@DiscriminatorValue("CAPITAN")
public class CapitanEntity extends JugadorEntity {

    @Column(name = "team_id")
    private Long teamId;

    public CapitanEntity() {}

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
