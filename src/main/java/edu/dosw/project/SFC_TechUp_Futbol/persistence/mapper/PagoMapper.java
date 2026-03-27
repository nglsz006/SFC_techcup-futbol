package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PagoEntity;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    private final EquipoMapper equipoMapper;

    public PagoMapper(EquipoMapper equipoMapper) {
        this.equipoMapper = equipoMapper;
    }

    public PagoEntity toEntity(Pago pago) {
        if (pago == null) {
            return null;
        }
        PagoEntity entity = new PagoEntity();
        entity.setId(pago.getId());
        entity.setComprobante(pago.getComprobante());
        entity.setFechaSubida(pago.getFechaSubida());
        entity.setEstado(pago.getEstado());
        entity.setEquipo(equipoMapper.toEntity(pago.getEquipo()));
        return entity;
    }

    public Pago toDomain(PagoEntity entity) {
        if (entity == null) {
            return null;
        }
        Pago pago = new Pago();
        pago.setId(entity.getId());
        pago.setComprobante(entity.getComprobante());
        pago.setFechaSubida(entity.getFechaSubida());
        pago.setEstado(entity.getEstado());
        pago.setState(resolverPagoState(entity.getEstado()));
        pago.setEquipo(equipoMapper.toDomain(entity.getEquipo()));
        return pago;
    }

    private PagoState resolverPagoState(Pago.PagoEstado estado) {
        if (estado == null) {
            return new PendienteState();
        }
        switch (estado) {
            case EN_REVISION:
                return new EnRevisionState();
            case APROBADO:
                return new AprobadoState();
            case RECHAZADO:
                return new RechazadoState();
            default:
                return new PendienteState();
        }
    }
}
