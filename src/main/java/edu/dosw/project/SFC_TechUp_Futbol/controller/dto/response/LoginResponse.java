package edu.dosw.project.SFC_TechUp_Futbol.dto.response;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class LoginResponse {
    private String token;
    private String nombre;
    private String email;
    private Usuario.TipoUsuario tipoUsuario;

    public LoginResponse(String token, String nombre, String email, Usuario.TipoUsuario tipoUsuario) {
        this.token = token;
        this.nombre = nombre;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public String getToken() { return token; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Usuario.TipoUsuario getTipoUsuario() { return tipoUsuario; }
}
