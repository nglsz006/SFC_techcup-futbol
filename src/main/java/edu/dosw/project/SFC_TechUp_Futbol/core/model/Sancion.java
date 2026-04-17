package edu.dosw.project.SFC_TechUp_Futbol.core.model;

public class Sancion {

    public enum TipoSancion {
        TARJETA_ROJA, TARJETA_AMARILLA, AGRESION_VERBAL, AGRESION_FISICA
    }

    private String id;
    private TipoSancion tipoSancion;
    private String descripcion;
    private Jugador jugador;

    public Sancion() {}

    public Sancion(String id, TipoSancion tipoSancion, String descripcion, Jugador jugador) {
        this.id = id;
        this.tipoSancion = tipoSancion;
        this.descripcion = descripcion;
        this.jugador = jugador;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public TipoSancion getTipoSancion() { return tipoSancion; }
    public void setTipoSancion(TipoSancion tipoSancion) { this.tipoSancion = tipoSancion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }
}
