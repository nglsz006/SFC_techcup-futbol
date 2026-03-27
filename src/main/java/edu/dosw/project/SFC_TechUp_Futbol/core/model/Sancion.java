package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import lombok.Data;

@Data
public class Sancion {

    public enum TipoSancion {
        TARJETA_ROJA,
        TARJETA_AMARILLA,
        AGRESION_VERBAL,
        AGRESION_FISICA
    }

    private Long id;
    private TipoSancion tipoSancion;
    private String descripcion;
    private Jugador jugador;

    public Sancion() {}

    public Sancion(Long id, TipoSancion tipoSancion, String descripcion, Jugador jugador) {
        this.id = id;
        this.tipoSancion = tipoSancion;
        this.descripcion = descripcion;
        this.jugador = jugador;
    }
}