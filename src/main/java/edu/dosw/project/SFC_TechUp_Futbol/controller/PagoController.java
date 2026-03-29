package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payments", description = "Tournament payment query. To upload or verify receipts use the Users endpoints (captain or organizer).")
@RestController
@RequestMapping("/api/payments")
public class PagoController {

    private final PagoService pagoService;
    private final PagoValidator pagoValidator;

    public PagoController(PagoService pagoService, PagoValidator pagoValidator) {
        this.pagoService = pagoService;
        this.pagoValidator = pagoValidator;
    }

    @Operation(summary = "Get payment by ID")
    @GetMapping("/{id}")
    public Pago consultarPago(@PathVariable String id) {
        return pagoService.consultarPago(id);
    }

    @Operation(summary = "Get payments by team")
    @GetMapping("/team/{teamId}")
    public List<Pago> consultarPagosPorEquipo(@PathVariable String teamId) {
        return pagoService.consultarPagosPorEquipo(teamId);
    }
}
