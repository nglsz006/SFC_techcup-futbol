package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.AdministradorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = Base64Util.class)
public interface AdministradorMapper {

    @Mapping(target = "email", expression = "java(Base64Util.encode(administrador.getEmail()))")
    @Mapping(target = "activo", source = "activo")
    AdministradorEntity toEntity(Administrador administrador);

    @Mapping(target = "email", expression = "java(Base64Util.decode(entity.getEmail()))")
    @Mapping(target = "activo", source = "activo")
    Administrador toDomain(AdministradorEntity entity);
}
