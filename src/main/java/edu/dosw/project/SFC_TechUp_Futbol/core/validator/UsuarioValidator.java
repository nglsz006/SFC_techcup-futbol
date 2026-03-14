package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import java.util.List;

public class UsuarioValidator {

    public boolean correoYaRegistrado(String correo, List<? extends Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(correo)) {
                return true;
            }
        }
        return false;
    }

    public boolean correoValido(String correo) {
        return correo != null && correo.contains("@") && correo.contains(".");
    }

    public boolean contrasenaValida(String contrasena) {
        return contrasena != null && contrasena.length() >= 8;
    }

    public boolean nombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }
}

