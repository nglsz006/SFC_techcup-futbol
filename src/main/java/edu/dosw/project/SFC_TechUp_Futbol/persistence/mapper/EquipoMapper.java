package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import org.springframework.stereotype.Component;

@Component
public class EquipoMapper {

    public EquipoEntity toEntity(Equipo equipo) {
        if (equipo == null) {
            return null;
        }
        EquipoEntity entity = new EquipoEntity();
        entity.setId((long) equipo.getId());
        entity.setNombre(equipo.getNombre());
        entity.setEscudo(equipo.getEscudo());
        entity.setColorPrincipal(equipo.getColorPrincipal());
        entity.setColorSecundario(equipo.getColorSecundario());
        entity.setCapitanId((long) equipo.getCapitanId());
        entity.setJugadores(equipo.getJugadores());
        return entity;
    }

    public Equipo toDomain(EquipoEntity entity) {
        if (entity == null) {
            return null;
        }
        Equipo equipo = new Equipo();
        equipo.setId(entity.getId().intValue());
        equipo.setNombre(entity.getNombre());
        equipo.setEscudo(entity.getEscudo());
        equipo.setColorPrincipal(entity.getColorPrincipal());
        equipo.setColorSecundario(entity.getColorSecundario());
        equipo.setCapitanId(entity.getCapitanId() != null ? entity.getCapitanId().intValue() : 0);
        equipo.setJugadores(entity.getJugadores());
        return equipo;
    }
}
