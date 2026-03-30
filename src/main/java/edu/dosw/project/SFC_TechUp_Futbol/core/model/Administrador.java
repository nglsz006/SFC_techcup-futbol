package edu.dosw.project.SFC_TechUp_Futbol.core.model;

public class Administrador extends Usuario {

    private boolean activo;

    public Administrador() {
        this.activo = true;
    }

    public Administrador(String id, String name, String email, String password, TipoUsuario userType, boolean activo) {
        super(id, name, email, password, userType);
        this.activo = activo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
