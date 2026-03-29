package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class UsuarioResponse {
    private String id;
    private String nombre;
    private String email;
    private Usuario.TipoUsuario tipoUsuario;

    public UsuarioResponse(String id, String nombre, String email, Usuario.TipoUsuario tipoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Usuario.TipoUsuario getTipoUsuario() { return tipoUsuario; }
}
