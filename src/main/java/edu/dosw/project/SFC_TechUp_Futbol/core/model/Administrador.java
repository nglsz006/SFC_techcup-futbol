package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {

    @Column(name = "activo")
    private boolean activo;

    public Administrador() {
        this.activo = true;
    }

    public Administrador(Long id, String name, String email, String password,
                         TipoUsuario userType, boolean activo) {
        super(id, name, email, password, userType);
        this.activo = activo;
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
