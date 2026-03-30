package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payments", description = "Tournament payment query. To upload or verify receipts use the Users endpoints (captain or organizer).")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final PagoValidator pagoValidator;

    public PagoController(PagoService pagoService, PagoValidator pagoValidator) {
        this.pagoService = pagoService;
        this.pagoValidator = pagoValidator;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get payment by ID", description = "Returns the status and detail of a specific payment.")
    @GetMapping("/{id}")
    public Pago consultarPago(@PathVariable Long id) {
        return pagoService.consultarPago(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get payments by team", description = "Lists all payments registered for a team.")
    @GetMapping("/equipo/{equipoId}")
    public List<Pago> consultarPagosPorEquipo(@PathVariable Long equipoId) {
        return pagoService.consultarPagosPorEquipo(equipoId);
    }
}
