package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import jakarta.persistence.*;

@Entity
@Table(name = "tarjeta")
public class TarjetaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Partido.Tarjeta.TipoTarjeta tipo;

    @Column(nullable = false)
    private int minuto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id")
    private JugadorEntity jugador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id", nullable = false)
    private PartidoEntity partido;

    public TarjetaEntity() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Partido.Tarjeta.TipoTarjeta getTipo() {
        return tipo;
    }

    public void setTipo(Partido.Tarjeta.TipoTarjeta tipo) {
        this.tipo = tipo;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public JugadorEntity getJugador() {
        return jugador;
    }

    public void setJugador(JugadorEntity jugador) {
        this.jugador = jugador;
    }

    public PartidoEntity getPartido() {
        return partido;
    }

    public void setPartido(PartidoEntity partido) {
        this.partido = partido;
    }
}
