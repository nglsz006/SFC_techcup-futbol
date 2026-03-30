package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Submit payment receipt")
    @PostMapping("/team/{teamId}/receipt")
    public Pago subirComprobante(@PathVariable String teamId, @RequestParam String comprobante) {
        pagoValidator.validarSubirComprobante(teamId, comprobante);
        return pagoService.subirComprobante(teamId, comprobante);
    }

    @Operation(summary = "Get payment by ID")
    @GetMapping("/{id}")
    public Pago consultarPago(@PathVariable String id) {
        return pagoService.consultarPago(id);
    }

    @Operation(summary = "Get payment status by ID")
    @GetMapping("/{id}/status")
    public Pago.PagoEstado consultarEstadoPago(@PathVariable String id) {
        return pagoService.consultarPago(id).getEstado();
    }

    @Operation(summary = "Get payments by team")
    @GetMapping("/team/{teamId}")
    public List<Pago> consultarPagosPorEquipo(@PathVariable String teamId) {
        return pagoService.consultarPagosPorEquipo(teamId);
    }
}
