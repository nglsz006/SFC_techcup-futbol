package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.service.PagoService;
import edu.dosw.project.SFC_TechUp_Futbol.validators.PagoValidator;

import java.util.List;
import java.util.Map;

public class PagoController {

    private final PagoService pagoService;
    private final PagoValidator pagoValidator;

    public PagoController(PagoService pagoService, PagoValidator pagoValidator) {
        this.pagoService = pagoService;
        this.pagoValidator = pagoValidator;
    }

    public Pago subirComprobante(Map<String, Object> body) {
        Long equipoId = Long.valueOf(body.get("equipoId").toString());
        String comprobante = body.get("comprobante").toString();
        pagoValidator.validarSubirComprobante(equipoId, comprobante);
        return pagoService.subirComprobante(equipoId, comprobante);
    }

    public Pago aprobarPago(Long id) {
        return pagoService.aprobarPago(id);
    }

    public Pago rechazarPago(Long id) {
        return pagoService.rechazarPago(id);
    }

    public Pago consultarPago(Long id) {
        return pagoService.consultarPago(id);
    }

    public List<Pago> consultarPagosPorEquipo(Long equipoId) {
        return pagoService.consultarPagosPorEquipo(equipoId);
    }

    public List<Pago> consultarPagosPendientes() {
        return pagoService.consultarPagosPendientes();
    }
}
