package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "perfil_deportivo")
public class PerfilDeportivoEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "jugador_id", nullable = false, unique = true)
    private String jugadorId;

    @ElementCollection
    @CollectionTable(name = "perfil_posiciones", joinColumns = @JoinColumn(name = "perfil_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "posicion")
    private List<Jugador.Posicion> posiciones = new ArrayList<>();

    @Column
    private int dorsal;

    @Column
    private String foto;

    @Column
    private int edad;

    @Enumerated(EnumType.STRING)
    @Column
    private PerfilDeportivo.Genero genero;

    @Column
    private String identificacion;

    @Column
    private Integer semestre;

    public PerfilDeportivoEntity() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public List<Jugador.Posicion> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(List<Jugador.Posicion> posiciones) {
        this.posiciones = posiciones;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public PerfilDeportivo.Genero getGenero() {
        return genero;
    }

    public void setGenero(PerfilDeportivo.Genero genero) {
        this.genero = genero;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
}
