package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.PagoEstado;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PagoRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final EquipoRepository equipoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository, EquipoRepository equipoRepository) {
        this.pagoRepository = pagoRepository;
        this.equipoRepository = equipoRepository;
    }

    @Override
    public Pago subirComprobante(Long equipoId, String comprobante) {
        Equipo equipo = equipoRepository.findById(equipoId.intValue())
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
            pago.avanzar();
        }
        pago.avanzar();
        return pagoRepository.save(pago);
    }

    @Override
    public Pago rechazarPago(Long pagoId) {
        Pago pago = getPagoOrThrow(pagoId);
        if (pago.getEstado() == PagoEstado.PENDIENTE) {
            pago.avanzar();
        }
        pago.rechazar();
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

