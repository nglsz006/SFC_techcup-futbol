package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.SancionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = JugadorMapper.class)
public interface SancionMapper {

    @Mapping(target = "partido", ignore = true)
    SancionEntity toEntity(Sancion sancion);

    Sancion toDomain(SancionEntity entity);
}
