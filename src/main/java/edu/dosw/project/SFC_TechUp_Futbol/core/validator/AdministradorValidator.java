package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import org.springframework.stereotype.Component;

@Component
public class AdministradorValidator {

    private final UsuarioValidator usuarioValidator = new UsuarioValidator();
    private final RegistroValidator registroValidator = new RegistroValidator();

    public void validarAdministradorId(Long administradorId) {
        if (administradorId == null || administradorId <= 0) {
            throw new IllegalArgumentException("El identificador del administrador es obligatorio.");
        }
    }

    public void validarRegistro(RegistroAdministrativoRequest request) {
        if (!usuarioValidator.nombreValido(request.getNombre())) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (!usuarioValidator.correoValido(request.getEmail())) {
            throw new IllegalArgumentException("El correo no tiene un formato valido.");
        }
        if (!usuarioValidator.contrasenaValida(request.getPassword())) {
            throw new IllegalArgumentException("La contrasena debe tener minimo 8 caracteres.");
        }
        if (!registroValidator.tipoUsuarioValido(request.getTipoUsuario())) {
            throw new IllegalArgumentException("El tipo de usuario es obligatorio.");
        }
        if (!registroValidator.correoValidoSegunTipo(request.getEmail(), request.getTipoUsuario())) {
            throw new IllegalArgumentException("El correo no corresponde al tipo de usuario.");
        }
    }
}
