package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.OrganizadorEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.TorneoJpaRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = TorneoMapper.class, imports = Base64Util.class)
public abstract class OrganizadorMapper {

    @Autowired
    protected TorneoJpaRepository torneoJpaRepository;

    @Autowired
    protected TorneoMapper torneoMapper;

    @Mapping(target = "email", expression = "java(Base64Util.encode(organizador.getEmail()))")
    @Mapping(target = "torneoId", expression = "java(organizador.getCurrentTournament() != null ? organizador.getCurrentTournament().getId() : null)")
    public abstract OrganizadorEntity toEntity(Organizador organizador);

    @Mapping(target = "email", expression = "java(Base64Util.decode(entity.getEmail()))")
    @Mapping(target = "currentTournament", ignore = true)
    public abstract Organizador toDomain(OrganizadorEntity entity);

    @AfterMapping
    protected void resolverTorneo(OrganizadorEntity entity, @MappingTarget Organizador organizador) {
        if (entity.getTorneoId() != null) {
            torneoJpaRepository.findById(entity.getTorneoId())
                    .ifPresent(t -> organizador.setCurrentTournament(torneoMapper.toDomain(t)));
        }
    }
}
