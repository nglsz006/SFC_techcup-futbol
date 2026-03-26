package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import java.time.LocalDateTime;

public class RegistroAuditoriaResponse {

    private final Long id;
    private final Long administradorId;
    private final String usuario;
    private final String tipoAccion;
    private final String descripcion;
    private final LocalDateTime fecha;

    public RegistroAuditoriaResponse(Long id, Long administradorId, String usuario, String tipoAccion,
                                     String descripcion, LocalDateTime fecha) {
        this.id = id;
        this.administradorId = administradorId;
        this.usuario = usuario;
        this.tipoAccion = tipoAccion;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public Long getAdministradorId() {
        return administradorId;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
