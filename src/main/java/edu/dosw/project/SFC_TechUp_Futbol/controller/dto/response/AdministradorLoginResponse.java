package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

public class AdministradorLoginResponse {

    private final Long administradorId;
    private final String nombre;
    private final String email;
    private final String token;

    public AdministradorLoginResponse(Long administradorId, String nombre, String email, String token) {
        this.administradorId = administradorId;
        this.nombre = nombre;
        this.email = email;
        this.token = token;
    }

    public Long getAdministradorId() {
        return administradorId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
