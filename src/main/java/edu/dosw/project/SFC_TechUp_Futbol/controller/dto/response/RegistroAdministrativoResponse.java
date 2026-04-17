package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class RegistroAdministrativoResponse {

    private final String id;
    private final String nombre;
    private final String email;
    private final Usuario.TipoUsuario tipoUsuario;
    private final String rol;
    private final String registradoPor;

    public RegistroAdministrativoResponse(String id, String nombre, String email, Usuario.TipoUsuario tipoUsuario,
                                          String rol, String registradoPor) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.rol = rol;
        this.registradoPor = registradoPor;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Usuario.TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public String getRol() { return rol; }
    public String getRegistradoPor() { return registradoPor; }
}
