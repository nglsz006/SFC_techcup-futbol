package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class OrganizadorResponse {

    private final String id;
    private final String nombre;
    private final String email;
    private final Usuario.TipoUsuario tipoUsuario;

    public OrganizadorResponse(Organizador o) {
        this.id = o.getId();
        this.nombre = o.getName();
        this.email = o.getEmail();
        this.tipoUsuario = o.getUserType();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Usuario.TipoUsuario getTipoUsuario() { return tipoUsuario; }
}
