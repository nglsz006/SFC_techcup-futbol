package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Arbitro;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.ArbitroEntity;
import org.springframework.stereotype.Component;

@Component
public class ArbitroMapper {

    private final PartidoMapper partidoMapper;

    public ArbitroMapper(PartidoMapper partidoMapper) {
        this.partidoMapper = partidoMapper;
    }

    public ArbitroEntity toEntity(Arbitro arbitro) {
        if (arbitro == null) return null;
        ArbitroEntity entity = new ArbitroEntity();
        entity.setId(arbitro.getId());
        entity.setName(arbitro.getName());
        entity.setEmail(arbitro.getEmail());
        entity.setPassword(arbitro.getPassword());
        entity.setUserType(arbitro.getUserType());
        if (arbitro.getAssignedMatches() != null) {
            entity.setAssignedMatches(
                arbitro.getAssignedMatches().stream()
                    .map(partidoMapper::toEntity)
                    .toList()
            );
        }
        return entity;
    }

    public Arbitro toDomain(ArbitroEntity entity) {
        if (entity == null) return null;
        Arbitro arbitro = new Arbitro();
        arbitro.setId(entity.getId());
        arbitro.setName(entity.getName());
        arbitro.setEmail(entity.getEmail());
        arbitro.setPassword(entity.getPassword());
        arbitro.setUserType(entity.getUserType());
        if (entity.getAssignedMatches() != null) {
            arbitro.setAssignedMatches(
                entity.getAssignedMatches().stream()
                    .map(partidoMapper::toDomain)
                    .toList()
            );
        }
        return arbitro;
    }
}
