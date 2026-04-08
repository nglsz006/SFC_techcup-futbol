package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Payments", description = "Tournament payment management.")
@RestController
@RequestMapping("/api/payments")
public class PagoController {

    private final PagoService pagoService;
    private final PagoValidator pagoValidator;

    public PagoController(PagoService pagoService, PagoValidator pagoValidator) {
        this.pagoService = pagoService;
        this.pagoValidator = pagoValidator;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Submit payment receipt")
    @PostMapping("/team/{teamId}/receipt")
    public Pago subirComprobante(@PathVariable String teamId, @RequestParam String comprobante) {
        pagoValidator.validarSubirComprobante(teamId, comprobante);
        return pagoService.subirComprobante(teamId, comprobante);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Send payment to review")
    @PatchMapping("/{id}/review")
    public Pago enviarARevision(@PathVariable String id) {
        return pagoService.enviarARevision(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get payment by ID")
    @GetMapping("/{id}")
    public Pago consultarPago(@PathVariable String id) {
        return pagoService.consultarPago(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get payment status by ID")
    @GetMapping("/{id}/status")
    public Pago.PagoEstado consultarEstadoPago(@PathVariable String id) {
        return pagoService.consultarPago(id).getEstado();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get payments by team")
    @GetMapping("/team/{teamId}")
    public List<Pago> consultarPagosPorEquipo(@PathVariable String teamId) {
        return pagoService.consultarPagosPorEquipo(teamId);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get payments by status", description = "Valid values: PENDIENTE, EN_REVISION, APROBADO, RECHAZADO")
    @GetMapping("/status/{estado}")
    public List<Pago> consultarPagosPorEstado(@PathVariable String estado) {
        Pago.PagoEstado pagoEstado;
        try {
            pagoEstado = Pago.PagoEstado.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado invalido. Use: PENDIENTE, EN_REVISION, APROBADO o RECHAZADO.");
        }
        return pagoService.consultarPagosPorEstado(pagoEstado);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Check if team is enabled", description = "Returns whether the team has an approved payment.")
    @GetMapping("/team/{teamId}/enabled")
    public Map<String, Object> equipoHabilitado(@PathVariable String teamId) {
        boolean habilitado = pagoService.equipoHabilitado(teamId);
        return Map.of("equipoId", teamId, "habilitado", habilitado);
    }
}
