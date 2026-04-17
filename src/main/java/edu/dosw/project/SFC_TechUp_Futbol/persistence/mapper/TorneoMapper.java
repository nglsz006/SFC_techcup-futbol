package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.EstadoTorneoInterface;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.TorneoCreado;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.TorneoEnCurso;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.TorneoFinalizado;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.TorneoEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TorneoMapper {

    TorneoEntity toEntity(Torneo torneo);

    @Mapping(target = "estadoObj", ignore = true)
    Torneo toDomain(TorneoEntity entity);

    @AfterMapping
    default void resolverEstadoObj(TorneoEntity entity, @MappingTarget Torneo torneo) {
        torneo.setEstadoObj(resolverEstado(entity.getEstado()));
    }

    default EstadoTorneoInterface resolverEstado(Torneo.EstadoTorneo estado) {
        if (estado == null) return new TorneoCreado();
        switch (estado) {
            case EN_CURSO: return new TorneoEnCurso();
            case FINALIZADO: return new TorneoFinalizado();
            default: return new TorneoCreado();
        }
    }
}
