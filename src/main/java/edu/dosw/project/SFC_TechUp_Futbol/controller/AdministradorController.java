package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.RegistroAdministrativoResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Administrators", description = "Administrative management for organizers and referees.")
@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;
    private final AdministradorValidator administradorValidator;

    public AdministradorController(AdministradorService administradorService,
                                   AdministradorValidator administradorValidator) {
        this.administradorService = administradorService;
        this.administradorValidator = administradorValidator;
    }

    @Operation(summary = "Create administrator", description = "Creates an administrator account that can register organizers and referees.")
    @PostMapping
    public RegistroAdministrativoResponse registrarAdministrador(@RequestBody RegistroAdministrativoRequest request) {
        administradorValidator.validarRegistro(request);
        Administrador administrador = administradorService.registrarAdministrador(request);
        return toResponse(administrador, "ADMINISTRADOR", null);
    }

    @Operation(summary = "Register organizer", description = "Protected endpoint. Requires an administrator id in the X-Administrador-Id header.")
    @PostMapping("/organizadores")
    public RegistroAdministrativoResponse registrarOrganizador(
            @RequestHeader("X-Administrador-Id") Long administradorId,
            @RequestBody RegistroAdministrativoRequest request) {
        administradorValidator.validarAdministradorId(administradorId);
        administradorValidator.validarRegistro(request);
        Organizador organizador = administradorService.registrarOrganizador(administradorId, request);
        return toResponse(organizador, "ORGANIZADOR", administradorId);
    }

    @Operation(summary = "Register referee", description = "Protected endpoint. Requires an administrator id in the X-Administrador-Id header.")
    @PostMapping("/arbitros")
    public RegistroAdministrativoResponse registrarArbitro(
            @RequestHeader("X-Administrador-Id") Long administradorId,
            @RequestBody RegistroAdministrativoRequest request) {
        administradorValidator.validarAdministradorId(administradorId);
        administradorValidator.validarRegistro(request);
        Arbitro arbitro = administradorService.registrarArbitro(administradorId, request);
        return toResponse(arbitro, "ARBITRO", administradorId);
    }

    private RegistroAdministrativoResponse toResponse(Usuario usuario, String rol, Long registradoPor) {
        return new RegistroAdministrativoResponse(
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                usuario.getUserType(),
                rol,
                registradoPor
        );
    }
}
