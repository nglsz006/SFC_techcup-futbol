package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "organizador")
@DiscriminatorValue("ORGANIZADOR")
public class OrganizadorEntity extends UsuarioEntity {

    @Column(name = "torneo_id")
    private Long torneoId;

    public OrganizadorEntity() {}

    public Long getTorneoId() {
        return torneoId;
    }

    public void setTorneoId(Long torneoId) {
        this.torneoId = torneoId;
    }
}
