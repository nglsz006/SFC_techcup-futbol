package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;

import java.util.List;

public interface PagoService {
    Pago subirComprobante(String equipoId, String comprobante);
    Pago enviarARevision(String pagoId);
    Pago aprobarPago(String pagoId);
    Pago rechazarPago(String pagoId);
    Pago consultarPago(String pagoId);
    List<Pago> consultarPagosPorEquipo(String equipoId);
    List<Pago> consultarPagosPendientes();
    List<Pago> consultarPagosPorEstado(Pago.PagoEstado estado);
    boolean equipoHabilitado(String equipoId);
}
