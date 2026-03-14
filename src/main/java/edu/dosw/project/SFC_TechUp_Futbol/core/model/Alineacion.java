package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import java.util.ArrayList;
import java.util.List;

public class Alineacion {
    private int id;
    private int equipoId;
    private int partidoId;
    private Formacion formacion;
    private List<Integer> titulares;
    private List<Integer> reservas;

    public Alineacion() {
        this.titulares = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    public Alineacion(int id, int equipoId, int partidoId, Formacion formacion) {
        this.id = id;
        this.equipoId = equipoId;
        this.partidoId = partidoId;
        this.formacion = formacion;
        this.titulares = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEquipoId() { return equipoId; }
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }

    public int getPartidoId() { return partidoId; }
    public void setPartidoId(int partidoId) { this.partidoId = partidoId; }

    public Formacion getFormacion() { return formacion; }
    public void setFormacion(Formacion formacion) { this.formacion = formacion; }

    public List<Integer> getTitulares() { return titulares; }
    public void setTitulares(List<Integer> titulares) { this.titulares = titulares; }

    public List<Integer> getReservas() { return reservas; }
    public void setReservas(List<Integer> reservas) { this.reservas = reservas; }
}

