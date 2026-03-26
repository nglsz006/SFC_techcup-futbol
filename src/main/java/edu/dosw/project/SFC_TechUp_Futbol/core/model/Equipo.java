package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String escudo;

    @Column(name = "color_principal", nullable = false)
    private String colorPrincipal;

    @Column(name = "color_secundario")
    private String colorSecundario;

    @Column(name = "capitan_id")
    private int capitanId;

    @ElementCollection
    @CollectionTable(name = "equipo_jugadores", joinColumns = @JoinColumn(name = "equipo_id"))
    @Column(name = "jugador_id")
    private List<Integer> jugadores = new ArrayList<>();

    public Equipo() {}

    public Equipo(int id, String nombre, String escudo, String colorPrincipal,
                  String colorSecundario, int capitanId) {
        this.id = id;
        this.nombre = nombre;
        this.escudo = escudo;
        this.colorPrincipal = colorPrincipal;
        this.colorSecundario = colorSecundario;
        this.capitanId = capitanId;
    }

    public void agregarJugador(int jugadorId) { this.jugadores.add(jugadorId); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }

    public String getColorPrincipal() { return colorPrincipal; }
    public void setColorPrincipal(String colorPrincipal) { this.colorPrincipal = colorPrincipal; }

    public String getColorSecundario() { return colorSecundario; }
    public void setColorSecundario(String colorSecundario) { this.colorSecundario = colorSecundario; }

    public int getCapitanId() { return capitanId; }
    public void setCapitanId(int capitanId) { this.capitanId = capitanId; }

    public List<Integer> getJugadores() { return jugadores; }
    public void setJugadores(List<Integer> jugadores) { this.jugadores = jugadores; }
}
