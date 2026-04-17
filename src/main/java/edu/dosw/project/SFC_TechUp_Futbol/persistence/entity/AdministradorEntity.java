package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "administrador")
@DiscriminatorValue("ADMINISTRADOR")
public class AdministradorEntity extends UsuarioEntity {

    @Column(nullable = false)
    private boolean activo;

    public AdministradorEntity() {
        this.activo = true;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
