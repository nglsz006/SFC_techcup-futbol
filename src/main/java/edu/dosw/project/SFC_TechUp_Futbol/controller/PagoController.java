package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pagos", description = "Consulta de pagos del torneo. Para subir o verificar comprobantes usa los endpoints de Usuarios (capitán u organizador).")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final PagoValidator pagoValidator;

    public PagoController(PagoService pagoService, PagoValidator pagoValidator) {
        this.pagoService = pagoService;
        this.pagoValidator = pagoValidator;
    }

    @Operation(summary = "Consultar pago por ID", description = "Retorna el estado y detalle de un pago específico.")
    @GetMapping("/{id}")
    public Pago consultarPago(@PathVariable Long id) {
        return pagoService.consultarPago(id);
    }

    @Operation(summary = "Consultar pagos por equipo", description = "Lista todos los pagos registrados para un equipo.")
    @GetMapping("/equipo/{equipoId}")
    public List<Pago> consultarPagosPorEquipo(@PathVariable Long equipoId) {
        return pagoService.consultarPagosPorEquipo(equipoId);
    }
}
