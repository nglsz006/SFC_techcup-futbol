package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "capitan")
@DiscriminatorValue("CAPITAN")
public class CapitanEntity extends JugadorEntity {

    @Column(name = "team_id")
    private String teamId;

    public CapitanEntity() {}

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
