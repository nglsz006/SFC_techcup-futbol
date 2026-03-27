package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import java.time.LocalDateTime;

public class RegistroAuditoria {

    private Long id;
    private Long administradorId;
    private String usuario;
    private TipoAccionAuditoria tipoAccion;
    private String descripcion;
    private LocalDateTime fecha;

    public RegistroAuditoria() {
    }

    public RegistroAuditoria(Long id, Long administradorId, String usuario, TipoAccionAuditoria tipoAccion,
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdministradorId() {
        return administradorId;
    }

    public void setAdministradorId(Long administradorId) {
        this.administradorId = administradorId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public TipoAccionAuditoria getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(TipoAccionAuditoria tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
