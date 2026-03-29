package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;
import jakarta.persistence.*;

@Entity
@Table(name = "sancion")
public class SancionEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sancion.TipoSancion tipoSancion;

    @Column(nullable = false)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id")
    private JugadorEntity jugador;

    public SancionEntity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Sancion.TipoSancion getTipoSancion() { return tipoSancion; }
    public void setTipoSancion(Sancion.TipoSancion tipoSancion) { this.tipoSancion = tipoSancion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public JugadorEntity getJugador() { return jugador; }
    public void setJugador(JugadorEntity jugador) { this.jugador = jugador; }
}
