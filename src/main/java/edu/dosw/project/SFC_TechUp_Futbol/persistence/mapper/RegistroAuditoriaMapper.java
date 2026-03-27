package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.RegistroAuditoriaEntity;
import org.springframework.stereotype.Component;

@Component
public class RegistroAuditoriaMapper {

    public RegistroAuditoriaEntity toEntity(RegistroAuditoria registro) {
        if (registro == null) {
            return null;
        }
        RegistroAuditoriaEntity entity = new RegistroAuditoriaEntity();
        entity.setId(registro.getId());
        entity.setAdministradorId(registro.getAdministradorId());
        entity.setUsuario(registro.getUsuario());
        entity.setTipoAccion(registro.getTipoAccion());
        entity.setDescripcion(registro.getDescripcion());
        entity.setFecha(registro.getFecha());
        return entity;
    }

    public RegistroAuditoria toDomain(RegistroAuditoriaEntity entity) {
        if (entity == null) {
            return null;
        }
        RegistroAuditoria registro = new RegistroAuditoria();
        registro.setId(entity.getId());
        registro.setAdministradorId(entity.getAdministradorId());
        registro.setUsuario(entity.getUsuario());
        registro.setTipoAccion(entity.getTipoAccion());
        registro.setDescripcion(entity.getDescripcion());
        registro.setFecha(entity.getFecha());
        return registro;
    }
}
