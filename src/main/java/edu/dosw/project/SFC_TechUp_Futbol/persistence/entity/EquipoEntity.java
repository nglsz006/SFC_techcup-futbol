package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipo")
public class EquipoEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String nombre;

    @Column
    private String escudo;

    @Column(name = "color_principal")
    private String colorPrincipal;

    @Column(name = "color_secundario")
    private String colorSecundario;

    @Column(name = "capitan_id")
    private String capitanId;

    @ElementCollection
    @CollectionTable(name = "equipo_jugadores", joinColumns = @JoinColumn(name = "equipo_id"))
    @Column(name = "jugador_id")
    private List<String> jugadores = new ArrayList<>();

    public EquipoEntity() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCapitanId() {
        return capitanId;
    }

    public void setCapitanId(String capitanId) {
        this.capitanId = capitanId;
    }

    public List<String> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<String> jugadores) {
        this.jugadores = jugadores;
    }
}
