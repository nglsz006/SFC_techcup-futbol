package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Administrador;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.AdministradorEntity;
import org.springframework.stereotype.Component;

@Component
public class AdministradorMapper {

    public AdministradorEntity toEntity(Administrador administrador) {
        if (administrador == null) {
            return null;
        }
        AdministradorEntity entity = new AdministradorEntity();
        entity.setId(administrador.getId());
        entity.setName(administrador.getName());
        entity.setEmail(administrador.getEmail());
        entity.setPassword(administrador.getPassword());
        entity.setUserType(administrador.getUserType());
        entity.setActivo(administrador.isActivo());
        return entity;
    }

    public Administrador toDomain(AdministradorEntity entity) {
        if (entity == null) {
            return null;
        }
        Administrador administrador = new Administrador();
        administrador.setId(entity.getId());
        administrador.setName(entity.getName());
        administrador.setEmail(entity.getEmail());
        administrador.setPassword(entity.getPassword());
        administrador.setUserType(entity.getUserType());
        administrador.setActivo(entity.isActivo());
        return administrador;
    }
}
