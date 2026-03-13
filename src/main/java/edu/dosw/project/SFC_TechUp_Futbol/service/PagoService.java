package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Pago;

import java.util.List;

public interface PagoService {
    Pago subirComprobante(Long equipoId, String comprobante);
    Pago aprobarPago(Long pagoId);
    Pago rechazarPago(Long pagoId);
    Pago consultarPago(Long pagoId);
    List<Pago> consultarPagosPorEquipo(Long equipoId);
    List<Pago> consultarPagosPendientes();
}
