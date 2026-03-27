package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "arbitro")
@DiscriminatorValue("ARBITRO")
public class ArbitroEntity extends UsuarioEntity {

    public ArbitroEntity() {}
}
