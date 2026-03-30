package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RecursoNoEncontradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ReglaNegocioException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PagoServiceImpl implements PagoService {

    private static final Logger log = Logger.getLogger(PagoServiceImpl.class.getName());

    private final PagoRepository pagoRepository;
    private final EquipoRepository equipoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository, EquipoRepository equipoRepository) {
        this.pagoRepository = pagoRepository;
        this.equipoRepository = equipoRepository;
    }

    @Override
    public Pago subirComprobante(String equipoId, String comprobante) {
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado con id: " + equipoId));

        if (pagoRepository.existsByEquipoIdAndEstado(equipoId, Pago.PagoEstado.APROBADO))
            throw new ReglaNegocioException("El equipo ya tiene un pago aprobado.");

        Pago pago = new Pago();
        pago.setComprobante(comprobante);
        pago.setEquipo(equipo);
        Pago saved = pagoRepository.save(pago);
        log.info("Comprobante subido para equipo: " + equipoId);
        return saved;
    }

    @Override
    public Pago aprobarPago(String pagoId) {
        Pago pago = getPagoOrThrow(pagoId);
        pago.avanzar();
        pago.avanzar();
        log.info("Pago aprobado: " + pagoId);
        return pagoRepository.save(pago);
    }

    @Override
    public Pago rechazarPago(String pagoId) {
        Pago pago = getPagoOrThrow(pagoId);
        pago.rechazar();
        log.info("Pago rechazado: " + pagoId);
        return pagoRepository.save(pago);
    }

    @Override
    public Pago consultarPago(String pagoId) {
        return getPagoOrThrow(pagoId);
    }

    @Override
    public List<Pago> consultarPagosPorEquipo(String equipoId) {
        return pagoRepository.findByEquipoId(equipoId);
    }

    @Override
    public List<Pago> consultarPagosPendientes() {
        return pagoRepository.findByEstado(Pago.PagoEstado.PENDIENTE);
    }

    private Pago getPagoOrThrow(String pagoId) {
        return pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado con id: " + pagoId));
    }
}
