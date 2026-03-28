package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.SancionEntity;
import org.springframework.stereotype.Component;

@Component
public class SancionMapper {

    private final JugadorMapper jugadorMapper;

    public SancionMapper(JugadorMapper jugadorMapper) {
        this.jugadorMapper = jugadorMapper;
    }

    public SancionEntity toEntity(Sancion sancion) {
        if (sancion == null) return null;
        SancionEntity entity = new SancionEntity();
        entity.setId(sancion.getId());
        entity.setTipoSancion(sancion.getTipoSancion());
        entity.setDescripcion(sancion.getDescripcion());
        entity.setJugador(jugadorMapper.toEntity(sancion.getJugador()));
        return entity;
    }

    public Sancion toDomain(SancionEntity entity) {
        if (entity == null) return null;
        Sancion sancion = new Sancion();
        sancion.setId(entity.getId());
        sancion.setTipoSancion(entity.getTipoSancion());
        sancion.setDescripcion(entity.getDescripcion());
        sancion.setJugador(jugadorMapper.toDomain(entity.getJugador()));
        return sancion;
    }
}
