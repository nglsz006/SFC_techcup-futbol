package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gol")
public class GolEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private int minuto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id")
    private JugadorEntity jugador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id", nullable = false)
    private PartidoEntity partido;

    public GolEntity() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
