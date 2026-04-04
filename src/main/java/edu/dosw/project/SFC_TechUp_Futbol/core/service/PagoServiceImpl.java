package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RecursoNoEncontradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ReglaNegocioException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PagoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.PagoJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PagoServiceImpl implements PagoService {

    private static final Logger log = Logger.getLogger(PagoServiceImpl.class.getName());

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    private final PagoJpaRepository pagoRepository;
    private final PagoMapper pagoMapper;
    private final EquipoJpaRepository equipoRepository;
    private final EquipoMapper equipoMapper;

    public PagoServiceImpl(PagoJpaRepository pagoRepository, PagoMapper pagoMapper,
                           EquipoJpaRepository equipoRepository, EquipoMapper equipoMapper) {
        this.pagoRepository = pagoRepository;
        this.pagoMapper = pagoMapper;
        this.equipoRepository = equipoRepository;
        this.equipoMapper = equipoMapper;
    }

    @Override
    public Pago subirComprobante(String equipoId, String comprobante) {
        Equipo equipo = equipoRepository.findById(equipoId)
                .map(equipoMapper::toDomain)
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipo no encontrado."));

        if (pagoRepository.existsByEquipoIdAndEstado(equipoId, Pago.PagoEstado.APROBADO))
            throw new ReglaNegocioException("El equipo ya tiene un pago aprobado.");

        Pago pago = new Pago();
        pago.setId(IdGeneratorUtil.generarId());
        pago.setComprobante(comprobante);
        pago.setEquipo(equipo);
        Pago saved = pagoMapper.toDomain(pagoRepository.save(pagoMapper.toEntity(pago)));
        log.info("Comprobante subido para equipo: " + sanitize(equipoId));
        return saved;
    }

    @Override
    public Pago aprobarPago(String pagoId) {
        Pago pago = getPagoOrThrow(pagoId);
        pago.avanzar();
        pago.avanzar();
        log.info("Pago aprobado: " + sanitize(pagoId));
        return pagoMapper.toDomain(pagoRepository.save(pagoMapper.toEntity(pago)));
    }

    @Override
    public Pago rechazarPago(String pagoId) {
        Pago pago = getPagoOrThrow(pagoId);
        pago.rechazar();
        log.info("Pago rechazado: " + sanitize(pagoId));
        return pagoMapper.toDomain(pagoRepository.save(pagoMapper.toEntity(pago)));
    }

    @Override
    public Pago consultarPago(String pagoId) {
        return getPagoOrThrow(pagoId);
    }

    @Override
    public List<Pago> consultarPagosPorEquipo(String equipoId) {
        return pagoRepository.findByEquipoId(equipoId).stream().map(pagoMapper::toDomain).toList();
    }

    @Override
    public List<Pago> consultarPagosPendientes() {
        return pagoRepository.findByEstado(Pago.PagoEstado.PENDIENTE).stream().map(pagoMapper::toDomain).toList();
    }

    private Pago getPagoOrThrow(String pagoId) {
        return pagoRepository.findById(pagoId)
                .map(pagoMapper::toDomain)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado."));
    }
}
