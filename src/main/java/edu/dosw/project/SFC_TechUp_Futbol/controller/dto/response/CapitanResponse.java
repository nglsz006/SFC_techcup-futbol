package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class CapitanResponse {

    private final String id;
    private final String nombre;
    private final String email;
    private final Usuario.TipoUsuario tipoUsuario;
    private final int numeroCamiseta;
    private final Jugador.Posicion posicion;

    public CapitanResponse(Capitan c) {
        this.id = c.getId();
        this.nombre = c.getName();
        this.email = c.getEmail();
        this.tipoUsuario = c.getUserType();
        this.numeroCamiseta = c.getJerseyNumber();
        this.posicion = c.getPosition();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Usuario.TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public int getNumeroCamiseta() { return numeroCamiseta; }
    public Jugador.Posicion getPosicion() { return posicion; }
}
