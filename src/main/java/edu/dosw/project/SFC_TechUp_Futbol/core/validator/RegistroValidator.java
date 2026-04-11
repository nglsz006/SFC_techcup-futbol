package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;

public class RegistroValidator {

    private static final String DOMINIO_INSTITUCIONAL = "@escuelaing.edu.co";
    private static final String DOMINIO_ESTUDIANTE = "@mail.escuelaing.edu.co";
    private static final String DOMINIO_GMAIL = "@gmail.com";

    public boolean correoInstitucionalValido(String correo) {
        return correo != null && (correo.endsWith(DOMINIO_INSTITUCIONAL) || correo.endsWith(DOMINIO_ESTUDIANTE));
    }

    public boolean correoGmailValido(String correo) {
        return correo != null && correo.endsWith(DOMINIO_GMAIL);
    }

    public boolean correoValidoSegunTipo(String correo, Usuario.TipoUsuario tipoUsuario) {
        if (tipoUsuario == Usuario.TipoUsuario.FAMILIAR) {
            return correoGmailValido(correo);
        }
        return correoInstitucionalValido(correo);
    }

    public boolean tipoUsuarioValido(Usuario.TipoUsuario tipoUsuario) {
        return tipoUsuario != null;
    }
}
