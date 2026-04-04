package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.UsuarioRegistradoEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioRegistradoMapper {

    public UsuarioRegistradoEntity toEntity(UsuarioRegistrado usuario) {
        if (usuario == null) return null;
        UsuarioRegistradoEntity entity = new UsuarioRegistradoEntity();
        entity.setId(usuario.getId());
        entity.setName(usuario.getName());
        entity.setEmail(Base64Util.encode(usuario.getEmail()));
        entity.setPassword(usuario.getPassword());
        entity.setUserType(usuario.getUserType());
        return entity;
    }

    public UsuarioRegistrado toDomain(UsuarioRegistradoEntity entity) {
        if (entity == null) return null;
        UsuarioRegistrado usuario = new UsuarioRegistrado();
        usuario.setId(entity.getId());
        usuario.setName(entity.getName());
        usuario.setEmail(Base64Util.decode(entity.getEmail()));
        usuario.setPassword(entity.getPassword());
        usuario.setUserType(entity.getUserType());
        return usuario;
    }
}
