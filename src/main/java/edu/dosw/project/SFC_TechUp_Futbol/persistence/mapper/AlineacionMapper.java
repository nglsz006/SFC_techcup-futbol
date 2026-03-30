package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.AlineacionEntity;
import org.springframework.stereotype.Component;

@Component
public class AlineacionMapper {

    public AlineacionEntity toEntity(Alineacion alineacion) {
        if (alineacion == null) {
            return null;
        }
        AlineacionEntity entity = new AlineacionEntity();
        entity.setId(alineacion.getId());
        entity.setEquipoId(alineacion.getEquipoId());
        entity.setPartidoId(alineacion.getPartidoId());
        entity.setFormacion(alineacion.getFormacion());
        entity.setTitulares(alineacion.getTitulares());
        entity.setReservas(alineacion.getReservas());
        return entity;
    }

    public Alineacion toDomain(AlineacionEntity entity) {
        if (entity == null) {
            return null;
        }
        Alineacion alineacion = new Alineacion();
        alineacion.setId(entity.getId());
        alineacion.setEquipoId(entity.getEquipoId());
        alineacion.setPartidoId(entity.getPartidoId());
        alineacion.setFormacion(entity.getFormacion());
        alineacion.setTitulares(entity.getTitulares());
        alineacion.setReservas(entity.getReservas());
        return alineacion;
    }
}
