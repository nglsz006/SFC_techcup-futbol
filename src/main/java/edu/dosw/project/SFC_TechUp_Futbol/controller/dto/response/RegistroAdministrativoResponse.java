package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class RegistroAdministrativoResponse {
    private final Long id;
    private final String nombre;
    private final String email;
    private final Usuario.TipoUsuario tipoUsuario;
    private final String rol;
    private final Long registradoPor;

    public RegistroAdministrativoResponse(Long id, String nombre, String email, Usuario.TipoUsuario tipoUsuario,
                                          String rol, Long registradoPor) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.rol = rol;
        this.registradoPor = registradoPor;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public Usuario.TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public String getRol() {
        return rol;
    }

    public Long getRegistradoPor() {
        return registradoPor;
    }
}
