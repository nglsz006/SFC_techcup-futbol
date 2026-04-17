package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.CapitanEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = EquipoMapper.class, imports = Base64Util.class)
public abstract class CapitanMapper {

    @Autowired
    protected EquipoJpaRepository equipoJpaRepository;

    @Autowired
    protected EquipoMapper equipoMapper;

    @Mapping(target = "email", expression = "java(Base64Util.encode(capitan.getEmail()))")
    @Mapping(target = "equipoId", expression = "java(capitan.getEquipo())")
    @Mapping(target = "teamId", expression = "java(capitan.getTeam() != null ? capitan.getTeam().getId() : null)")
    public abstract CapitanEntity toEntity(Capitan capitan);

    @Mapping(target = "email", expression = "java(Base64Util.decode(entity.getEmail()))")
    @Mapping(target = "equipo", source = "equipoId")
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "sanciones", ignore = true)
    public abstract Capitan toDomain(CapitanEntity entity);

    @AfterMapping
    protected void resolverTeam(CapitanEntity entity, @MappingTarget Capitan capitan) {
        if (entity.getTeamId() != null) {
            equipoJpaRepository.findById(entity.getTeamId())
                    .ifPresent(e -> capitan.setTeam(equipoMapper.toDomain(e)));
        }
    }
}
