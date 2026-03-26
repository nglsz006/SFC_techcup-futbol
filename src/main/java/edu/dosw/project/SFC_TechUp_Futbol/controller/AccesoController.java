package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.service.AccesoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AccesoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.RegistroAuditoriaRepositoryImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Access", description = "Registration and login for all system actors.")
@RestController
@RequestMapping("/api/acceso")
public class AccesoController {

    private final AccesoService authService;
    private final AccesoValidator authValidator;
    private final AuditoriaService auditoriaService;

    public AccesoController(AccesoService authService, AccesoValidator authValidator) {
        this(authService, authValidator, new AuditoriaService(new RegistroAuditoriaRepositoryImpl()));
    }

    @Autowired
    public AccesoController(AccesoService authService, AccesoValidator authValidator, AuditoriaService auditoriaService) {
        this.authService = authService;
        this.authValidator = authValidator;
        this.auditoriaService = auditoriaService;
    }

    @Operation(summary = "Register user", description = "Creates a new account in the system. Available for Players and Captains.")
    @PostMapping("/registro")
    public UsuarioResponse registrar(@RequestBody RegistroRequest request) {
        authValidator.validarRegistro(request);
        UsuarioResponse response = authService.registrar(request);
        auditoriaService.registrarEvento(
                response.getId(),
                response.getEmail(),
                TipoAccionAuditoria.REGISTRO_USUARIO,
                "Registro de usuario en la plataforma."
        );
        return response;
    }

    @Operation(summary = "Login", description = "Authenticates the user and returns their session data.")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        authValidator.validarLogin(request);
        LoginResponse response = authService.login(request);
        auditoriaService.registrarEvento(
                null,
                response.getEmail(),
                TipoAccionAuditoria.LOGIN_USUARIO,
                "Inicio de sesion de usuario."
        );
        return response;
    }
}
