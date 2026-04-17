package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PerfilDeportivoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = Base64Util.class)
public interface PerfilDeportivoMapper {

    @Mapping(target = "identificacion", expression = "java(Base64Util.encode(perfil.getIdentificacion()))")
    PerfilDeportivoEntity toEntity(PerfilDeportivo perfil);

    @Mapping(target = "identificacion", expression = "java(Base64Util.decode(entity.getIdentificacion()))")
    PerfilDeportivo toDomain(PerfilDeportivoEntity entity);
}
