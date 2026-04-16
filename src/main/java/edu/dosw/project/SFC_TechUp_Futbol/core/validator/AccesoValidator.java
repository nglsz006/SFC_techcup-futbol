package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import org.springframework.stereotype.Component;

@Component
public class AccesoValidator {

    private final UsuarioValidator usuarioValidator = new UsuarioValidator();
    private final RegistroValidator registroValidator = new RegistroValidator();

    public void validarLogin(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank())
            throw new IllegalArgumentException("El correo es obligatorio.");
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new IllegalArgumentException("La contrasena es obligatoria.");
        if (!usuarioValidator.correoValido(request.getEmail()))
            throw new IllegalArgumentException("El correo no tiene un formato valido.");
    }

    public void validarRegistro(RegistroRequest request) {
        if (!usuarioValidator.nombreValido(request.getNombre()))
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (!usuarioValidator.correoValido(request.getEmail()))
            throw new IllegalArgumentException("El correo no tiene un formato valido.");
        if (!usuarioValidator.contrasenaValida(request.getPassword()))
            throw new IllegalArgumentException("La contrasena debe tener minimo 8 caracteres.");
        if (request.getTipoUsuario() == null)
            throw new IllegalArgumentException("El tipo de usuario es obligatorio.");
        if (!registroValidator.correoValidoSegunTipo(request.getEmail(), request.getTipoUsuario()))
            throw new IllegalArgumentException("El correo no corresponde al tipo de usuario.");
    }
}
