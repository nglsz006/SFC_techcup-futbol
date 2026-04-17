package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipoMapper {

    EquipoEntity toEntity(Equipo equipo);

    Equipo toDomain(EquipoEntity entity);
}
