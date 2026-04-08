package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PagoEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = EquipoMapper.class, imports = Base64Util.class)
public interface PagoMapper {

    @Mapping(target = "comprobante", expression = "java(Base64Util.encode(pago.getComprobante()))")
    PagoEntity toEntity(Pago pago);

    @Mapping(target = "comprobante", expression = "java(Base64Util.decode(entity.getComprobante()))")
    @Mapping(target = "state", ignore = true)
    Pago toDomain(PagoEntity entity);

    @AfterMapping
    default void resolverPagoState(PagoEntity entity, @MappingTarget Pago pago) {
        pago.setState(resolverEstado(entity.getEstado()));
    }

    default PagoState resolverEstado(Pago.PagoEstado estado) {
        if (estado == null) return new PendienteState();
        switch (estado) {
            case EN_REVISION: return new EnRevisionState();
            case APROBADO: return new AprobadoState();
            case RECHAZADO: return new RechazadoState();
            default: return new PendienteState();
        }
    }
}
