package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import java.time.LocalDateTime;

public class RegistroAuditoria {

    private String id;
    private String administradorId;
    private String usuario;
    private TipoAccionAuditoria tipoAccion;
    private String descripcion;
    private LocalDateTime fecha;

    public RegistroAuditoria() {}

    public RegistroAuditoria(String id, String administradorId, String usuario, TipoAccionAuditoria tipoAccion,
                             String descripcion, LocalDateTime fecha) {
        this.id = id;
        this.administradorId = administradorId;
        this.usuario = usuario;
        this.tipoAccion = tipoAccion;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAdministradorId() { return administradorId; }
    public void setAdministradorId(String administradorId) { this.administradorId = administradorId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public TipoAccionAuditoria getTipoAccion() { return tipoAccion; }
    public void setTipoAccion(TipoAccionAuditoria tipoAccion) { this.tipoAccion = tipoAccion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
