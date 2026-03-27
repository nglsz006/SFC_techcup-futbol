package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import java.util.List;

public class PerfilDeportivo {

    public enum Genero {
        MASCULINO, FEMENINO, OTRO
    }

    private Long id;
    private Long jugadorId;
    private List<Jugador.Posicion> posiciones;
    private int dorsal;
    private String foto;
    private int edad;
    private Genero genero;
    private String identificacion;
    private Integer semestre;

    public PerfilDeportivo() {}

    public PerfilDeportivo(Long id, Long jugadorId, List<Jugador.Posicion> posiciones,
                           int dorsal, String foto, int edad, Genero genero,
                           String identificacion, Integer semestre) {
        this.id = id;
        this.jugadorId = jugadorId;
        this.posiciones = posiciones;
        this.dorsal = dorsal;
        this.foto = foto;
        this.edad = edad;
        this.genero = genero;
        this.identificacion = identificacion;
        this.semestre = semestre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getJugadorId() { return jugadorId; }
    public void setJugadorId(Long jugadorId) { this.jugadorId = jugadorId; }

    public List<Jugador.Posicion> getPosiciones() { return posiciones; }
    public void setPosiciones(List<Jugador.Posicion> posiciones) { this.posiciones = posiciones; }

    public int getDorsal() { return dorsal; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
}
