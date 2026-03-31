package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class ArbitroResponse {

    private final String id;
    private final String nombre;
    private final String email;
    private final Usuario.TipoUsuario tipoUsuario;

    public ArbitroResponse(Arbitro a) {
        this.id = a.getId();
        this.nombre = a.getName();
        this.email = a.getEmail();
        this.tipoUsuario = a.getUserType();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Usuario.TipoUsuario getTipoUsuario() { return tipoUsuario; }
}
