package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class JugadorResponse {

    private final String id;
    private final String nombre;
    private final String email;
    private final Usuario.TipoUsuario tipoUsuario;
    private final int numeroCamiseta;
    private final Jugador.Posicion posicion;
    private final boolean disponible;

    public JugadorResponse(Jugador j) {
        this.id = j.getId();
        this.nombre = j.getName();
        this.email = j.getEmail();
        this.tipoUsuario = j.getUserType();
        this.numeroCamiseta = j.getJerseyNumber();
        this.posicion = j.getPosition();
        this.disponible = j.isAvailable();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Usuario.TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public int getNumeroCamiseta() { return numeroCamiseta; }
    public Jugador.Posicion getPosicion() { return posicion; }
    public boolean isDisponible() { return disponible; }
}
