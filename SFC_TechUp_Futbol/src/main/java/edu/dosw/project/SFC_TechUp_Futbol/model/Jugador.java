package edu.dosw.project.SFC_TechUp_Futbol.model;

public class Jugador {
    private Long id;
    private String nombre;
    private Equipo equipo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
}
