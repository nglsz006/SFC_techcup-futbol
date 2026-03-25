package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class RegistroAdministrativoRequest {
    private String nombre;
    private String email;
    private String password;
    private Usuario.TipoUsuario tipoUsuario;
    private String rol;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario.TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Usuario.TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
