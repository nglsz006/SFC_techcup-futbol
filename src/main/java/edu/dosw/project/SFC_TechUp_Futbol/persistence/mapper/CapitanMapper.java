package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.CapitanEntity;
import org.springframework.stereotype.Component;

@Component
public class CapitanMapper {

    public CapitanEntity toEntity(Capitan capitan) {
        if (capitan == null) {
            return null;
        }
        CapitanEntity entity = new CapitanEntity();
        entity.setId(capitan.getId());
        entity.setName(capitan.getName());
        entity.setEmail(capitan.getEmail());
        entity.setPassword(capitan.getPassword());
        entity.setUserType(capitan.getUserType());
        entity.setJerseyNumber(capitan.getJerseyNumber());
        entity.setPosition(capitan.getPosition());
        entity.setAvailable(capitan.isAvailable());
        entity.setPhoto(capitan.getPhoto());
        entity.setEquipoId(capitan.getEquipo());
        if (capitan.getTeam() != null) {
            entity.setTeamId(capitan.getTeam().getId());
        }
        return entity;
    }

    public Capitan toDomain(CapitanEntity entity) {
        if (entity == null) {
            return null;
        }
        Capitan capitan = new Capitan();
        capitan.setId(entity.getId());
        capitan.setName(entity.getName());
        capitan.setEmail(entity.getEmail());
        capitan.setPassword(entity.getPassword());
        capitan.setUserType(entity.getUserType());
        capitan.setJerseyNumber(entity.getJerseyNumber());
        capitan.setPosition(entity.getPosition());
        capitan.setAvailable(entity.isAvailable());
        capitan.setPhoto(entity.getPhoto());
        capitan.setEquipo(entity.getEquipoId());
        // team se resuelve en el servicio si se necesita
        return capitan;
    }
}
