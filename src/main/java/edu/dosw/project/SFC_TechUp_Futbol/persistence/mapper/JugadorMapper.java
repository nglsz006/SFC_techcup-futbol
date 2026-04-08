package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = Base64Util.class)
public interface JugadorMapper {

    @Mapping(target = "email", expression = "java(Base64Util.encode(jugador.getEmail()))")
    @Mapping(target = "equipoId", expression = "java(jugador.getEquipo())")
    JugadorEntity toEntity(Jugador jugador);

    @Mapping(target = "email", expression = "java(Base64Util.decode(entity.getEmail()))")
    @Mapping(target = "equipo", source = "equipoId")
    @Mapping(target = "sanciones", ignore = true)
    Jugador toDomain(JugadorEntity entity);
}
