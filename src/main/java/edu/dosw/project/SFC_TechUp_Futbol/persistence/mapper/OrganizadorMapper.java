package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.OrganizadorEntity;
import org.springframework.stereotype.Component;

@Component
public class OrganizadorMapper {

    public OrganizadorEntity toEntity(Organizador organizador) {
        if (organizador == null) {
            return null;
        }
        OrganizadorEntity entity = new OrganizadorEntity();
        entity.setId(organizador.getId());
        entity.setName(organizador.getName());
        entity.setEmail(organizador.getEmail());
        entity.setPassword(organizador.getPassword());
        entity.setUserType(organizador.getUserType());
        if (organizador.getCurrentTournament() != null) {
            entity.setTorneoId(organizador.getCurrentTournament().getId());
        }
        return entity;
    }

    public Organizador toDomain(OrganizadorEntity entity) {
        if (entity == null) {
            return null;
        }
        Organizador organizador = new Organizador();
        organizador.setId(entity.getId());
        organizador.setName(entity.getName());
        organizador.setEmail(entity.getEmail());
        organizador.setPassword(entity.getPassword());
        organizador.setUserType(entity.getUserType());
        // currentTournament se resuelve en el servicio si se necesita
        return organizador;
    }
}
