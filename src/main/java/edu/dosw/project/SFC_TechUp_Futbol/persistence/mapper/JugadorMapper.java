package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity;
import org.springframework.stereotype.Component;

@Component
public class JugadorMapper {

    public JugadorEntity toEntity(Jugador jugador) {
        if (jugador == null) {
            return null;
        }
        JugadorEntity entity = new JugadorEntity();
        entity.setId(jugador.getId());
        entity.setName(jugador.getName());
        entity.setEmail(jugador.getEmail());
        entity.setPassword(jugador.getPassword());
        entity.setUserType(jugador.getUserType());
        entity.setJerseyNumber(jugador.getJerseyNumber());
        entity.setPosition(jugador.getPosition());
        entity.setAvailable(jugador.isAvailable());
        entity.setPhoto(jugador.getPhoto());
        entity.setEquipoId(jugador.getEquipo());
        return entity;
    }

    public Jugador toDomain(JugadorEntity entity) {
        if (entity == null) {
            return null;
        }
        Jugador jugador = new Jugador();
        jugador.setId(entity.getId());
        jugador.setName(entity.getName());
        jugador.setEmail(entity.getEmail());
        jugador.setPassword(entity.getPassword());
        jugador.setUserType(entity.getUserType());
        jugador.setJerseyNumber(entity.getJerseyNumber());
        jugador.setPosition(entity.getPosition());
        jugador.setAvailable(entity.isAvailable());
        jugador.setPhoto(entity.getPhoto());
        jugador.setEquipo(entity.getEquipoId());
        return jugador;
    }
}
