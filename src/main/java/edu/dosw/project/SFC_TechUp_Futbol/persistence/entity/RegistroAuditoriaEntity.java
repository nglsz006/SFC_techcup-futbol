package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_auditoria")
public class RegistroAuditoriaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "administrador_id", nullable = false)
    private String administradorId;

    @Column(nullable = false)
    private String usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_accion", nullable = false)
    private TipoAccionAuditoria tipoAccion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public RegistroAuditoriaEntity() {}

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
