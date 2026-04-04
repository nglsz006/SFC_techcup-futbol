package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PartidoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.TorneoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.PartidoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.TorneoJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@org.springframework.stereotype.Service
public class PartidoServiceImpl implements PartidoService {

    private static final Logger log = Logger.getLogger(PartidoServiceImpl.class.getName());

    private final PartidoJpaRepository partidoRepository;
    private final PartidoMapper partidoMapper;
    private final TorneoJpaRepository torneoRepository;
    private final TorneoMapper torneoMapper;
    private final EquipoJpaRepository equipoRepository;
    private final EquipoMapper equipoMapper;
    private final JugadorJpaRepository jugadorRepository;
    private final JugadorMapper jugadorMapper;

    public PartidoServiceImpl(PartidoJpaRepository partidoRepository, PartidoMapper partidoMapper,
                              TorneoJpaRepository torneoRepository, TorneoMapper torneoMapper,
                              EquipoJpaRepository equipoRepository, EquipoMapper equipoMapper,
                              JugadorJpaRepository jugadorRepository, JugadorMapper jugadorMapper) {
        this.partidoRepository = partidoRepository;
        this.partidoMapper = partidoMapper;
        this.torneoRepository = torneoRepository;
        this.torneoMapper = torneoMapper;
        this.equipoRepository = equipoRepository;
        this.equipoMapper = equipoMapper;
        this.jugadorRepository = jugadorRepository;
        this.jugadorMapper = jugadorMapper;
    }

    @Override
    public Partido crearPartido(String torneoId, String equipoLocalId, String equipoVisitanteId,
                                LocalDateTime fecha, String cancha) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .map(torneoMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado."));
        Equipo local = equipoRepository.findById(equipoLocalId)
                .map(equipoMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Equipo local no encontrado."));
        Equipo visitante = equipoRepository.findById(equipoVisitanteId)
                .map(equipoMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Equipo visitante no encontrado."));

        if (local.getId().equals(visitante.getId()))
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");

        Partido partido = new Partido();
        partido.setId(IdGeneratorUtil.generarId());
        partido.setTorneo(torneo);
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.setFecha(fecha);
        partido.setCancha(cancha);
        Partido saved = partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido)));
        log.info("Partido creado exitosamente");
        return saved;
    }

    @Override
    public Partido iniciarPartido(String partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.iniciar();
        log.info("Partido iniciado");
        return partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido)));
    }

    @Override
    public Partido registrarResultado(String partidoId, int golesLocal, int golesVisitante) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.registrarResultado(golesLocal, golesVisitante);
        log.info("Resultado registrado");
        return partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido)));
    }

    @Override
    public Partido finalizarPartido(String partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.finalizar();
        log.info("Partido finalizado");
        return partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido)));
    }

    @Override
    public Partido registrarGoleador(String partidoId, String jugadorId, int minuto) {
        Partido partido = getPartidoOrThrow(partidoId);
        validarPartidoEnCurso(partido);

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .map(jugadorMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado."));

        Partido.Gol gol = new Partido.Gol();
        gol.setId(IdGeneratorUtil.generarId());
        gol.setJugador(jugador);
        gol.setMinuto(minuto);
        partido.getGoles().add(gol);

        boolean esLocal = jugador.getEquipo() != null && partido.getEquipoLocal().getId().equals(jugador.getEquipo());
        if (esLocal) {
            partido.setMarcadorLocal(partido.getMarcadorLocal() + 1);
        } else {
            partido.setMarcadorVisitante(partido.getMarcadorVisitante() + 1);
        }
        log.info("Gol registrado");
        return partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido)));
    }

    @Override
    public Partido registrarSancion(String partidoId, String jugadorId, Sancion.TipoSancion tipoSancion, String descripcion) {
        Partido partido = getPartidoOrThrow(partidoId);
        validarPartidoEnCurso(partido);

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .map(jugadorMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado."));

        Sancion sancion = new Sancion();
        sancion.setId(IdGeneratorUtil.generarId());
        sancion.setJugador(jugador);
        sancion.setTipoSancion(tipoSancion);
        sancion.setDescripcion(descripcion);
        partido.getSanciones().add(sancion);
        jugador.agregarSancion(sancion);

        log.info("Sanción registrada");
        return partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido)));
    }

    @Override
    public Partido consultarPartido(String partidoId) {
        return getPartidoOrThrow(partidoId);
    }

    @Override
    public List<Partido> consultarPartidosPorTorneo(String torneoId) {
        return partidoRepository.findByTorneoId(torneoId).stream().map(partidoMapper::toDomain).toList();
    }

    @Override
    public List<Partido> consultarPartidosPorEquipo(String equipoId) {
        return partidoRepository.findByEquipoLocalIdOrEquipoVisitanteId(equipoId, equipoId).stream().map(partidoMapper::toDomain).toList();
    }

    private void validarPartidoEnCurso(Partido partido) {
        if (partido.getEstado() != Partido.PartidoEstado.EN_CURSO)
            throw new IllegalStateException("El partido no esta EN_CURSO.");
    }

    private Partido getPartidoOrThrow(String id) {
        return partidoRepository.findById(id)
                .map(partidoMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado."));
    }
}