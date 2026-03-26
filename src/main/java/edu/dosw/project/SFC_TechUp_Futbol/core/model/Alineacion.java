package edu.dosw.project.SFC_TechUp_Futbol.core.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alineaciones")
public class Alineacion {

    public enum Formacion {
        F_4_4_2("4-4-2"), F_4_3_3("4-3-3"), F_3_5_2("3-5-2"), F_4_5_1("4-5-1"), F_5_3_2("5-3-2");

        private final String valor;
        Formacion(String valor) { this.valor = valor; }
        public String getValor() { return valor; }

        public static Formacion fromString(String texto) {
            for (Formacion f : values()) {
                if (f.valor.equals(texto)) return f;
            }
            return null;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "equipo_id")
    private int equipoId;

    @Column(name = "partido_id")
    private int partidoId;

    @Enumerated(EnumType.STRING)
    @Column
    private Formacion formacion;

    @ElementCollection
    @CollectionTable(name = "alineacion_titulares", joinColumns = @JoinColumn(name = "alineacion_id"))
    @Column(name = "jugador_id")
    private List<Integer> titulares = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "alineacion_reservas", joinColumns = @JoinColumn(name = "alineacion_id"))
    @Column(name = "jugador_id")
    private List<Integer> reservas = new ArrayList<>();

    public Alineacion() {}

    public Alineacion(int id, int equipoId, int partidoId, Formacion formacion) {
        this.id = id;
        this.equipoId = equipoId;
        this.partidoId = partidoId;
        this.formacion = formacion;
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
