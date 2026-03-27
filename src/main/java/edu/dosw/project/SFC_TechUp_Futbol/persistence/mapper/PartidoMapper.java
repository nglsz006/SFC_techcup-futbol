package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.GolEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PartidoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.TarjetaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PartidoMapper {

    private final TorneoMapper torneoMapper;
    private final EquipoMapper equipoMapper;
    private final JugadorMapper jugadorMapper;

    public PartidoMapper(TorneoMapper torneoMapper, EquipoMapper equipoMapper, JugadorMapper jugadorMapper) {
        this.torneoMapper = torneoMapper;
        this.equipoMapper = equipoMapper;
        this.jugadorMapper = jugadorMapper;
    }

    public PartidoEntity toEntity(Partido partido) {
        if (partido == null) {
            return null;
        }
        PartidoEntity entity = new PartidoEntity();
        entity.setId(partido.getId());
        entity.setFecha(partido.getFecha());
        entity.setCancha(partido.getCancha());
        entity.setMarcadorLocal(partido.getMarcadorLocal());
        entity.setMarcadorVisitante(partido.getMarcadorVisitante());
        entity.setEstado(partido.getEstado());
        entity.setTorneo(torneoMapper.toEntity(partido.getTorneo()));
        entity.setEquipoLocal(equipoMapper.toEntity(partido.getEquipoLocal()));
        entity.setEquipoVisitante(equipoMapper.toEntity(partido.getEquipoVisitante()));
        entity.setGoles(golesAEntity(partido.getGoles(), entity));
        entity.setTarjetas(tarjetasAEntity(partido.getTarjetas(), entity));
        return entity;
    }

    public Partido toDomain(PartidoEntity entity) {
        if (entity == null) {
            return null;
        }
        Partido partido = new Partido();
        partido.setId(entity.getId());
        partido.setFecha(entity.getFecha());
        partido.setCancha(entity.getCancha());
        partido.setMarcadorLocal(entity.getMarcadorLocal());
        partido.setMarcadorVisitante(entity.getMarcadorVisitante());
        partido.setEstado(entity.getEstado());
        partido.setState(resolverPartidoState(entity.getEstado()));
        partido.setTorneo(torneoMapper.toDomain(entity.getTorneo()));
        partido.setEquipoLocal(equipoMapper.toDomain(entity.getEquipoLocal()));
        partido.setEquipoVisitante(equipoMapper.toDomain(entity.getEquipoVisitante()));
        partido.setGoles(golesADomain(entity.getGoles()));
        partido.setTarjetas(tarjetasADomain(entity.getTarjetas()));
        return partido;
    }

    private List<GolEntity> golesAEntity(List<Partido.Gol> goles, PartidoEntity partidoEntity) {
        List<GolEntity> lista = new ArrayList<>();
        if (goles == null) {
            return lista;
        }
        for (Partido.Gol gol : goles) {
            GolEntity golEntity = new GolEntity();
            golEntity.setId(gol.getId());
            golEntity.setMinuto(gol.getMinuto());
            golEntity.setJugador(jugadorMapper.toEntity(gol.getJugador()));
            golEntity.setPartido(partidoEntity);
            lista.add(golEntity);
        }
        return lista;
    }

    private List<Partido.Gol> golesADomain(List<GolEntity> entities) {
        List<Partido.Gol> lista = new ArrayList<>();
        if (entities == null) {
            return lista;
        }
        for (GolEntity golEntity : entities) {
            Partido.Gol gol = new Partido.Gol();
            gol.setId(golEntity.getId());
            gol.setMinuto(golEntity.getMinuto());
            gol.setJugador(jugadorMapper.toDomain(golEntity.getJugador()));
            lista.add(gol);
        }
        return lista;
    }

    private List<TarjetaEntity> tarjetasAEntity(List<Partido.Tarjeta> tarjetas, PartidoEntity partidoEntity) {
        List<TarjetaEntity> lista = new ArrayList<>();
        if (tarjetas == null) {
            return lista;
        }
        for (Partido.Tarjeta tarjeta : tarjetas) {
            TarjetaEntity tarjetaEntity = new TarjetaEntity();
            tarjetaEntity.setId(tarjeta.getId());
            tarjetaEntity.setTipo(tarjeta.getTipo());
            tarjetaEntity.setMinuto(tarjeta.getMinuto());
            tarjetaEntity.setJugador(jugadorMapper.toEntity(tarjeta.getJugador()));
            tarjetaEntity.setPartido(partidoEntity);
            lista.add(tarjetaEntity);
        }
        return lista;
    }

    private List<Partido.Tarjeta> tarjetasADomain(List<TarjetaEntity> entities) {
        List<Partido.Tarjeta> lista = new ArrayList<>();
        if (entities == null) {
            return lista;
        }
        for (TarjetaEntity tarjetaEntity : entities) {
            Partido.Tarjeta tarjeta = new Partido.Tarjeta();
            tarjeta.setId(tarjetaEntity.getId());
            tarjeta.setTipo(tarjetaEntity.getTipo());
            tarjeta.setMinuto(tarjetaEntity.getMinuto());
            tarjeta.setJugador(jugadorMapper.toDomain(tarjetaEntity.getJugador()));
            lista.add(tarjeta);
        }
        return lista;
    }

    private PartidoState resolverPartidoState(Partido.PartidoEstado estado) {
        if (estado == null) {
            return new ProgramadoState();
        }
        switch (estado) {
            case EN_CURSO:
                return new EnCursoState();
            case FINALIZADO:
                return new FinalizadoPartidoState();
            default:
                return new ProgramadoState();
        }
    }
}
