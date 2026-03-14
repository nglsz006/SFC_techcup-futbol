package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PagoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PagoValidator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final PagoValidator pagoValidator;

    public PagoController(PagoService pagoService, PagoValidator pagoValidator) {
        this.pagoService = pagoService;
        this.pagoValidator = pagoValidator;
    }

    record ComprobanteRequest(Long equipoId, String comprobante) {}

    @PostMapping
    public Pago subirComprobante(@RequestBody ComprobanteRequest req) {
        pagoValidator.validarSubirComprobante(req.equipoId(), req.comprobante());
        return pagoService.subirComprobante(req.equipoId(), req.comprobante());
    }

    @PutMapping("/{id}/aprobar")
    public Pago aprobarPago(@PathVariable Long id) {
        return pagoService.aprobarPago(id);
    }

    @PutMapping("/{id}/rechazar")
    public Pago rechazarPago(@PathVariable Long id) {
        return pagoService.rechazarPago(id);
    }

    @GetMapping("/{id}")
    public Pago consultarPago(@PathVariable Long id) {
        return pagoService.consultarPago(id);
    }

    @GetMapping("/equipo/{equipoId}")
    public List<Pago> consultarPagosPorEquipo(@PathVariable Long equipoId) {
        return pagoService.consultarPagosPorEquipo(equipoId);
    }

    @GetMapping("/pendientes")
    public List<Pago> consultarPagosPendientes() {
        return pagoService.consultarPagosPendientes();
    }
}
