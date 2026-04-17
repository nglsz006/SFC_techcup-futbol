package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.UsuarioRegistradoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = Base64Util.class)
public interface UsuarioRegistradoMapper {

    @Mapping(target = "email", expression = "java(Base64Util.encode(usuario.getEmail()))")
    UsuarioRegistradoEntity toEntity(UsuarioRegistrado usuario);

    @Mapping(target = "email", expression = "java(Base64Util.decode(entity.getEmail()))")
    UsuarioRegistrado toDomain(UsuarioRegistradoEntity entity);
}
