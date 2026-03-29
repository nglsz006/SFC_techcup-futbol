package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.AdministradorLoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.RegistroAdministrativoResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AutenticacionAdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "Administrative registration of organizers and referees.")
@RestController
@RequestMapping("/api/admin")
public class AdministradorController {

    private final AdministradorService administradorService;
    private final AdministradorValidator administradorValidator;
    private final AutenticacionAdministradorService autenticacionAdministradorService;
    private final AuditoriaService auditoriaService;

    public AdministradorController(AdministradorService administradorService,
                                   AdministradorValidator administradorValidator,
                                   AutenticacionAdministradorService autenticacionAdministradorService,
                                   AuditoriaService auditoriaService) {
        this.administradorService = administradorService;
        this.administradorValidator = administradorValidator;
        this.autenticacionAdministradorService = autenticacionAdministradorService;
        this.auditoriaService = auditoriaService;
    }

    @Operation(summary = "Admin login", description = "Authenticates an administrator and returns a session token.")
    @PostMapping("/login")
    public AdministradorLoginResponse login(@RequestBody LoginRequest request) {
        administradorValidator.validarCredenciales(request);
        String token = autenticacionAdministradorService.login(request.getEmail(), request.getPassword());
        Administrador administrador = administradorService.obtenerAdministradorPorEmail(request.getEmail());
        auditoriaService.registrarEvento(
                administrador.getId(),
                administrador.getEmail(),
                TipoAccionAuditoria.LOGIN_ADMIN,
                "Inicio de sesion del administrador."
        );
        return new AdministradorLoginResponse(
                administrador.getId(),
                administrador.getName(),
                administrador.getEmail(),
                token
        );
    }

    @Operation(summary = "Register organizer or referee", description = "Protected endpoint that only accepts roles ORGANIZADOR and ARBITRO.")
    @PostMapping("/users")
    public RegistroAdministrativoResponse registrarUsuario(
            @RequestHeader("X-Administrador-Id") Long administradorId,
            @RequestHeader("X-Administrador-Token") String token,
            @RequestBody RegistroAdministrativoRequest request) {
        administradorValidator.validarAdministradorId(administradorId);
        autenticacionAdministradorService.validarSesion(administradorId, token);
        administradorValidator.validarRegistro(request);
        Usuario usuario = administradorService.registrarUsuarioAdministrativo(administradorId, request);
        return new RegistroAdministrativoResponse(
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                usuario.getUserType(),
                request.getRol().toUpperCase(),
                administradorId
        );
    }
}
