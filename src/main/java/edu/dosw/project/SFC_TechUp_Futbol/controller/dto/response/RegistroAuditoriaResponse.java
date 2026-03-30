package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import java.time.LocalDateTime;

public class RegistroAuditoriaResponse {

    private final String id;
    private final String administradorId;
    private final String usuario;
    private final String tipoAccion;
    private final String descripcion;
    private final LocalDateTime fecha;

    public RegistroAuditoriaResponse(String id, String administradorId, String usuario, String tipoAccion,
                                     String descripcion, LocalDateTime fecha) {
        this.id = id;
        this.administradorId = administradorId;
        this.usuario = usuario;
        this.tipoAccion = tipoAccion;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public String getAdministradorId() { return administradorId; }
    public String getUsuario() { return usuario; }
    public String getTipoAccion() { return tipoAccion; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFecha() { return fecha; }
}
