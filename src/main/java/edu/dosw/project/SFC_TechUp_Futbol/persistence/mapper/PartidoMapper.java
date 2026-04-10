package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.GolEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PartidoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.SancionEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.TarjetaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = { TorneoMapper.class, EquipoMapper.class, JugadorMapper.class })
public abstract class PartidoMapper {

    @Autowired
    protected JugadorMapper jugadorMapper;

    @Mapping(target = "goles", ignore = true)
    @Mapping(target = "tarjetas", ignore = true)
    @Mapping(target = "sanciones", ignore = true)
    public abstract PartidoEntity toEntity(Partido partido);

    @Mapping(target = "goles", ignore = true)
    @Mapping(target = "tarjetas", ignore = true)
    @Mapping(target = "sanciones", ignore = true)
    @Mapping(target = "state", ignore = true)
    public abstract Partido toDomain(PartidoEntity entity);

    @AfterMapping
    protected void mapGolesYEstadoToEntity(Partido partido, @MappingTarget PartidoEntity entity) {
        List<GolEntity> goles = new ArrayList<>();
        if (partido.getGoles() != null) {
            for (Partido.Gol gol : partido.getGoles()) {
                GolEntity g = new GolEntity();
                g.setId(gol.getId() != null ? gol.getId() : UUID.randomUUID().toString());
                g.setMinuto(gol.getMinuto());
                g.setJugador(jugadorMapper.toEntity(gol.getJugador()));
                g.setPartido(entity);
                goles.add(g);
            }
        }
        entity.setGoles(goles);

        List<TarjetaEntity> tarjetas = new ArrayList<>();
        if (partido.getTarjetas() != null) {
            for (Partido.Tarjeta t : partido.getTarjetas()) {
                TarjetaEntity te = new TarjetaEntity();
                te.setId(t.getId() != null ? t.getId() : UUID.randomUUID().toString());
                te.setTipo(t.getTipo());
                te.setMinuto(t.getMinuto());
                te.setJugador(jugadorMapper.toEntity(t.getJugador()));
                te.setPartido(entity);
                tarjetas.add(te);
            }
        }
        entity.setTarjetas(tarjetas);

        List<SancionEntity> sanciones = new ArrayList<>();
        if (partido.getSanciones() != null) {
            for (Sancion s : partido.getSanciones()) {
                SancionEntity se = new SancionEntity();
                se.setId(s.getId());
                se.setTipoSancion(s.getTipoSancion());
                se.setDescripcion(s.getDescripcion());
                se.setJugador(jugadorMapper.toEntity(s.getJugador()));
                se.setPartido(entity);
                sanciones.add(se);
            }
        }
        entity.setSanciones(sanciones);
    }

    @AfterMapping
    protected void mapGolesYEstadoToDomain(PartidoEntity entity, @MappingTarget Partido partido) {
        List<Partido.Gol> goles = new ArrayList<>();
        if (entity.getGoles() != null) {
            for (GolEntity g : entity.getGoles()) {
                Partido.Gol gol = new Partido.Gol();
                gol.setId(g.getId());
                gol.setMinuto(g.getMinuto());
                gol.setJugador(jugadorMapper.toDomain(g.getJugador()));
                goles.add(gol);
            }
        }
        partido.setGoles(goles);

        List<Partido.Tarjeta> tarjetas = new ArrayList<>();
        if (entity.getTarjetas() != null) {
            for (TarjetaEntity te : entity.getTarjetas()) {
                Partido.Tarjeta t = new Partido.Tarjeta();
                t.setId(te.getId());
                t.setTipo(te.getTipo());
                t.setMinuto(te.getMinuto());
                t.setJugador(jugadorMapper.toDomain(te.getJugador()));
                tarjetas.add(t);
            }
        }
        partido.setTarjetas(tarjetas);

        List<Sancion> sanciones = new ArrayList<>();
        if (entity.getSanciones() != null) {
            for (SancionEntity se : entity.getSanciones()) {
                Sancion s = new Sancion();
                s.setId(se.getId());
                s.setTipoSancion(se.getTipoSancion());
                s.setDescripcion(se.getDescripcion());
                s.setJugador(jugadorMapper.toDomain(se.getJugador()));
                sanciones.add(s);
            }
        }
        partido.setSanciones(sanciones);
        partido.setState(resolverEstado(entity.getEstado()));
    }

    protected PartidoState resolverEstado(Partido.PartidoEstado estado) {
        if (estado == null) return new ProgramadoState();
        switch (estado) {
            case EN_CURSO: return new EnCursoState();
            case FINALIZADO: return new FinalizadoPartidoState();
            default: return new ProgramadoState();
        }
    }
}
