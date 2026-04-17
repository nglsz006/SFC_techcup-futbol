package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.RegistroAdministrativoResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AutenticacionAdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @Operation(summary = "Admin login")
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        administradorValidator.validarCredenciales(request);
        String token = autenticacionAdministradorService.login(request.getEmail(), request.getPassword());
        String administradorId = autenticacionAdministradorService.getAdministradorId(token);
        return Map.of("token", token, "administradorId", administradorId);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Register organizer or referee")
    @PostMapping("/users")
    public RegistroAdministrativoResponse registrarUsuario(
            @RequestHeader("X-Administrador-Id") String administradorId,
            @RequestHeader("X-Administrador-Token") String token,
            @RequestBody RegistroAdministrativoRequest request) {
        administradorValidator.validarAdministradorId(administradorId);
        autenticacionAdministradorService.validarSesion(administradorId, token);
        administradorValidator.validarRegistro(request);
        Usuario usuario = administradorService.registrarUsuarioAdministrativo(administradorId, request);
        return new RegistroAdministrativoResponse(
                usuario.getId(), usuario.getName(), usuario.getEmail(),
                usuario.getUserType(), request.getRol(), administradorId);
    }
}
