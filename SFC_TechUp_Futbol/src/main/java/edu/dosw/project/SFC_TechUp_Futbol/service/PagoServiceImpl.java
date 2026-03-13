package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.model.PagoEstado;
import edu.dosw.project.SFC_TechUp_Futbol.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.repository.PagoRepository;

import java.util.List;

public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final EquipoRepository equipoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository, EquipoRepository equipoRepository) {
        this.pagoRepository = pagoRepository;
        this.equipoRepository = equipoRepository;
    }

    @Override
    public Pago subirComprobante(Long equipoId, String comprobante) {
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con id: " + equipoId));

        if (pagoRepository.existsByEquipoIdAndEstado(equipoId, PagoEstado.APROBADO)) {
            throw new IllegalStateException("El equipo ya tiene un pago aprobado.");
        }

        Pago pago = new Pago();
        pago.setComprobante(comprobante);
        pago.setEquipo(equipo);
        return pagoRepository.save(pago);
    }

    @Override
    public Pago aprobarPago(Long pagoId) {
        Pago pago = getPagoOrThrow(pagoId);
        if (pago.getEstado() == PagoEstado.PENDIENTE) {
            pago.avanzar(); // PENDIENTE → EN_REVISION
        }
        pago.avanzar();     // EN_REVISION → APROBADO
        return pagoRepository.save(pago);
    }

    @Override
    public Pago rechazarPago(Long pagoId) {
        Pago pago = getPagoOrThrow(pagoId);
        if (pago.getEstado() == PagoEstado.PENDIENTE) {
            pago.avanzar(); // PENDIENTE → EN_REVISION
        }
        pago.rechazar();    // EN_REVISION → RECHAZADO
        return pagoRepository.save(pago);
    }

    @Override
    public Pago consultarPago(Long pagoId) {
        return getPagoOrThrow(pagoId);
    }

    @Override
    public List<Pago> consultarPagosPorEquipo(Long equipoId) {
        return pagoRepository.findByEquipoId(equipoId);
    }

    @Override
    public List<Pago> consultarPagosPendientes() {
        return pagoRepository.findByEstado(PagoEstado.PENDIENTE);
    }

    private Pago getPagoOrThrow(Long pagoId) {
        return pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + pagoId));
    }
}
