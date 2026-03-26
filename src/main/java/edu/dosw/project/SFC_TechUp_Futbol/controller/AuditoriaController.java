package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.ConsultaAuditoriaRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.ConsultaAuditoriaResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.RegistroAuditoriaResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AutenticacionAdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AuditoriaValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Auditoria", description = "Consulta de historial de acciones administrativas.")
@RestController
@RequestMapping("/api/admin/auditoria")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;
    private final AuditoriaValidator auditoriaValidator;
    private final AdministradorValidator administradorValidator;
    private final AutenticacionAdministradorService autenticacionAdministradorService;

    public AuditoriaController(AuditoriaService auditoriaService, AuditoriaValidator auditoriaValidator,
                               AdministradorValidator administradorValidator,
                               AutenticacionAdministradorService autenticacionAdministradorService) {
        this.auditoriaService = auditoriaService;
        this.auditoriaValidator = auditoriaValidator;
        this.administradorValidator = administradorValidator;
        this.autenticacionAdministradorService = autenticacionAdministradorService;
    }

    @Operation(summary = "Consultar auditoria", description = "Permite consultar el historial completo con filtros opcionales.")
    @GetMapping
    public ConsultaAuditoriaResponse consultarHistorial(
            @RequestHeader("X-Administrador-Id") Long administradorId,
            @RequestHeader("X-Administrador-Token") String token,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String tipoAccion,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {
        administradorValidator.validarAdministradorId(administradorId);
        autenticacionAdministradorService.validarSesion(administradorId, token);

        ConsultaAuditoriaRequest request = new ConsultaAuditoriaRequest();
        request.setUsuario(usuario);
        request.setTipoAccion(tipoAccion);
        request.setFechaDesde(fechaDesde);
        request.setFechaHasta(fechaHasta);
        auditoriaValidator.validarConsulta(request);

        List<RegistroAuditoriaResponse> registros = auditoriaService.consultarHistorial(request).stream()
                .map(this::toResponse)
                .toList();

        String mensaje = registros.isEmpty()
                ? "No se encontraron registros para los filtros indicados."
                : "Consulta realizada correctamente.";
        return new ConsultaAuditoriaResponse(mensaje, registros);
    }

    private RegistroAuditoriaResponse toResponse(RegistroAuditoria registro) {
        return new RegistroAuditoriaResponse(
                registro.getId(),
                registro.getAdministradorId(),
                registro.getUsuario(),
                registro.getTipoAccion().name(),
                registro.getDescripcion(),
                registro.getFecha()
        );
    }
}
