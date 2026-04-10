package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.AlineacionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlineacionMapper {

    AlineacionEntity toEntity(Alineacion alineacion);

    Alineacion toDomain(AlineacionEntity entity);
}
