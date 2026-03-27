package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipo")
public class EquipoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String escudo;

    @Column(name = "color_principal")
    private String colorPrincipal;

    @Column(name = "color_secundario")
    private String colorSecundario;

    @Column(name = "capitan_id")
    private Long capitanId;

    @ElementCollection
    @CollectionTable(name = "equipo_jugadores", joinColumns = @JoinColumn(name = "equipo_id"))
    @Column(name = "jugador_id")
    private List<Integer> jugadores = new ArrayList<>();

    public EquipoEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEscudo() {
        return escudo;
    }

    public void setEscudo(String escudo) {
        this.escudo = escudo;
    }

    public String getColorPrincipal() {
        return colorPrincipal;
    }

    public void setColorPrincipal(String colorPrincipal) {
        this.colorPrincipal = colorPrincipal;
    }

    public String getColorSecundario() {
        return colorSecundario;
    }

    public void setColorSecundario(String colorSecundario) {
        this.colorSecundario = colorSecundario;
    }

    public Long getCapitanId() {
        return capitanId;
    }

    public void setCapitanId(Long capitanId) {
        this.capitanId = capitanId;
    }

    public List<Integer> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Integer> jugadores) {
        this.jugadores = jugadores;
    }
}
