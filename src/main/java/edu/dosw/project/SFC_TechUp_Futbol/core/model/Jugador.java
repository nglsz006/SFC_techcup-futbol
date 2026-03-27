package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Jugador extends Usuario {

    public enum Posicion { PORTERO, DEFENSA, VOLANTE, DELANTERO }

    private int jerseyNumber;
    private Posicion position;
    private boolean available;
    private String photo;
    private Integer equipoId;
    private ArrayList<Sancion> sanciones = new ArrayList<>();

    public Jugador() {}

    public Jugador(Long id, String name, String email, String password, TipoUsuario userType, int jerseyNumber, Posicion position, boolean available, String photo) {
        super(id, name, email, password, userType);
        this.jerseyNumber = jerseyNumber;
        this.position = position;
        this.available = available;
        this.photo = photo;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public Posicion getPosition() {
        return position;
    }

    public void setPosition(Posicion position) {
        this.position = position;
    }

    public boolean isAvailable() {
        return available;
    }

public void setAvailable(boolean available) {
        this.available = available;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public Integer getEquipo() { return equipoId; }
    public void setEquipo(Integer equipoId) { this.equipoId = equipoId; }

    public ArrayList<Sancion> getSanciones() {
        return sanciones;
    }
    public void setSanciones(ArrayList<Sancion> sanciones) {
        this.sanciones = sanciones;
    }
    public void agregarSancion(Sancion sancion) {
        this.sanciones.add(sancion);
    }
    public List<Sancion> getSancionesPorTipo(Sancion.TipoSancion tipo) {
        return sanciones.stream()
                .filter(s -> s.getTipoSancion() == tipo)
                .collect(Collectors.toList());
    }
    public boolean tieneSancion(Sancion.TipoSancion tipo) {
        return sanciones.stream()
                .anyMatch(s -> s.getTipoSancion() == tipo);
    }
}
