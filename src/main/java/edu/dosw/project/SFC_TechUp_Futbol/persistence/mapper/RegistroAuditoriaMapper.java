package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RegistroAuditoria;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.RegistroAuditoriaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistroAuditoriaMapper {

    RegistroAuditoriaEntity toEntity(RegistroAuditoria registro);

    RegistroAuditoria toDomain(RegistroAuditoriaEntity entity);
}
