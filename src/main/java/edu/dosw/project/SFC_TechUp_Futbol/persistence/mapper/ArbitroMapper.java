package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.ArbitroEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", imports = Base64Util.class)
public abstract class ArbitroMapper {

    @Autowired
    protected PartidoMapper partidoMapper;

    @Mapping(target = "email", expression = "java(Base64Util.encode(arbitro.getEmail()))")
    @Mapping(target = "assignedMatches", ignore = true)
    public abstract ArbitroEntity toEntity(Arbitro arbitro);

    @Mapping(target = "email", expression = "java(Base64Util.decode(entity.getEmail()))")
    @Mapping(target = "assignedMatches", ignore = true)
    public abstract Arbitro toDomain(ArbitroEntity entity);

    @AfterMapping
    protected void mapAssignedMatchesToEntity(Arbitro arbitro, @MappingTarget ArbitroEntity entity) {
        if (arbitro.getAssignedMatches() != null) {
            entity.setAssignedMatches(
                arbitro.getAssignedMatches().stream()
                    .map(partidoMapper::toEntity)
                    .toList()
            );
        }
    }

    @AfterMapping
    protected void mapAssignedMatchesToDomain(ArbitroEntity entity, @MappingTarget Arbitro arbitro) {
        if (entity.getAssignedMatches() != null) {
            arbitro.setAssignedMatches(
                entity.getAssignedMatches().stream()
                    .map(partidoMapper::toDomain)
                    .toList()
            );
        }
    }
}
