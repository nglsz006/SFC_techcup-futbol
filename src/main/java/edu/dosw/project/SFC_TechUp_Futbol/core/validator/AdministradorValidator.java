package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AdministradorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.ArbitroJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.OrganizadorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.UsuarioRegistradoJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class AdministradorValidator {

    private final UsuarioValidator usuarioValidator = new UsuarioValidator();
    private final RegistroValidator registroValidator = new RegistroValidator();
    private final AdministradorJpaRepository administradorRepository;
    private final OrganizadorJpaRepository organizadorRepository;
    private final ArbitroJpaRepository arbitroRepository;
    private final UsuarioRegistradoJpaRepository usuarioRegistradoRepository;

    public AdministradorValidator(AdministradorJpaRepository administradorRepository,
                                  OrganizadorJpaRepository organizadorRepository,
                                  ArbitroJpaRepository arbitroRepository,
                                  UsuarioRegistradoJpaRepository usuarioRegistradoRepository) {
        this.administradorRepository = administradorRepository;
        this.organizadorRepository = organizadorRepository;
        this.arbitroRepository = arbitroRepository;
        this.usuarioRegistradoRepository = usuarioRegistradoRepository;
    }

    public void validarAdministradorId(String administradorId) {
        if (administradorId == null || administradorId.isBlank()) {
            throw new IllegalArgumentException("El identificador del administrador es obligatorio.");
        }
    }

    public void validarCredenciales(LoginRequest request) {
        if (request == null) throw new IllegalArgumentException("La solicitud de autenticacion es obligatoria.");
        if (!usuarioValidator.correoValido(request.getEmail()))
            throw new IllegalArgumentException("El correo no tiene un formato valido.");
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new IllegalArgumentException("La contrasena es obligatoria.");
    }

    public void validarRegistro(RegistroAdministrativoRequest request) {
        if (request == null) throw new IllegalArgumentException("La solicitud es obligatoria.");
        if (!usuarioValidator.nombreValido(request.getNombre()))
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (!usuarioValidator.correoValido(request.getEmail()))
            throw new IllegalArgumentException("El correo no tiene un formato valido.");
        if (!usuarioValidator.contrasenaValida(request.getPassword()))
            throw new IllegalArgumentException("La contrasena debe tener minimo 8 caracteres.");
        if (!registroValidator.tipoUsuarioValido(request.getTipoUsuario()))
            throw new IllegalArgumentException("El tipo de usuario es obligatorio.");
        if (!registroValidator.correoValidoSegunTipo(request.getEmail(), request.getTipoUsuario()))
            throw new IllegalArgumentException("El correo no corresponde al tipo de usuario.");
        if (request.getRol() == null || request.getRol().isBlank())
            throw new IllegalArgumentException("El rol es obligatorio.");
        if (!rolPermitido(request.getRol()))
            throw new RolNoPermitidoException("Solo se permite registrar usuarios con rol ORGANIZADOR o ARBITRO.");
        if (correoYaRegistrado(request.getEmail()))
            throw new CorreoYaRegistradoException("Ya existe un usuario con ese correo.");
    }

    private boolean rolPermitido(String rol) {
        return "ORGANIZADOR".equalsIgnoreCase(rol) || "ARBITRO".equalsIgnoreCase(rol);
    }

    private boolean correoYaRegistrado(String email) {
        String emailEncoded = Base64Util.encode(email);
        return administradorRepository.findByEmail(emailEncoded).isPresent()
                || organizadorRepository.findByEmail(emailEncoded).isPresent()
                || arbitroRepository.findByEmail(emailEncoded).isPresent()
                || usuarioRegistradoRepository.findByEmail(emailEncoded).isPresent();
    }
}
