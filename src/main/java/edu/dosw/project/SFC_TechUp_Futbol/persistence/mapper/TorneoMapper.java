package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.TorneoCreado;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.TorneoEnCurso;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.TorneoFinalizado;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.TorneoEntity;
import org.springframework.stereotype.Component;

@Component
public class TorneoMapper {

    public TorneoEntity toEntity(Torneo torneo) {
        if (torneo == null) {
            return null;
        }
        TorneoEntity entity = new TorneoEntity();
        entity.setId(torneo.getId());
        entity.setNombre(torneo.getNombre());
        entity.setFechaInicio(torneo.getFechaInicio());
        entity.setFechaFin(torneo.getFechaFin());
        entity.setCantidadEquipos(torneo.getCantidadEquipos());
        entity.setCosto(torneo.getCosto());
        entity.setEstado(torneo.getEstado());
        entity.setReglamento(torneo.getReglamento());
        entity.setCierreInscripciones(torneo.getCierreInscripciones());
        entity.setCanchas(torneo.getCanchas());
        entity.setHorarios(torneo.getHorarios());
        entity.setSanciones(torneo.getSanciones());
        return entity;
    }

    public Torneo toDomain(TorneoEntity entity) {
        if (entity == null) {
            return null;
        }
        Torneo torneo = new Torneo();
        torneo.setId(entity.getId());
        torneo.setNombre(entity.getNombre());
        torneo.setFechaInicio(entity.getFechaInicio());
        torneo.setFechaFin(entity.getFechaFin());
        torneo.setCantidadEquipos(entity.getCantidadEquipos());
        torneo.setCosto(entity.getCosto());
        torneo.setEstado(entity.getEstado());
        torneo.setReglamento(entity.getReglamento());
        torneo.setCierreInscripciones(entity.getCierreInscripciones());
        torneo.setCanchas(entity.getCanchas());
        torneo.setHorarios(entity.getHorarios());
        torneo.setSanciones(entity.getSanciones());
        torneo.setEstadoObj(resolverEstadoObj(entity.getEstado()));
        return torneo;
    }

    private edu.dosw.project.SFC_TechUp_Futbol.core.model.state.EstadoTorneoInterface resolverEstadoObj(Torneo.EstadoTorneo estado) {
        if (estado == null) {
            return new TorneoCreado();
        }
        switch (estado) {
            case EN_CURSO:
                return new TorneoEnCurso();
            case FINALIZADO:
                return new TorneoFinalizado();
            default:
                return new TorneoCreado();
        }
    }
}
