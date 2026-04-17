package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "organizador")
@DiscriminatorValue("ORGANIZADOR")
public class OrganizadorEntity extends UsuarioEntity {

    @Column(name = "torneo_id")
    private String torneoId;

    public OrganizadorEntity() {}

    public String getTorneoId() {
        return torneoId;
    }

    public void setTorneoId(String torneoId) {
        this.torneoId = torneoId;
    }
}
