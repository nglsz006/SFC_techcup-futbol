package edu.dosw.project.SFC_TechUp_Futbol.core.model;

public class UsuarioRegistrado extends Usuario {

    public UsuarioRegistrado() {}

    public UsuarioRegistrado(Long id, String nombre, String email, String password, TipoUsuario tipoUsuario) {
        super(id, nombre, email, password, tipoUsuario);
    }
}
