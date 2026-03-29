package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import java.util.logging.Logger;

@org.springframework.stereotype.Service
public class PartidoServiceImpl implements PartidoService {

    private static final Logger log = Logger.getLogger(PartidoServiceImpl.class.getName());

    private final PartidoRepository partidoRepository;
    private final TorneoRepository torneoRepository;
    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;

    public PartidoServiceImpl(PartidoRepository partidoRepository,
                              TorneoRepository torneoRepository,
                              EquipoRepository equipoRepository,
                              JugadorRepository jugadorRepository) {
        this.partidoRepository = partidoRepository;
        this.torneoRepository = torneoRepository;
        this.equipoRepository = equipoRepository;
        this.jugadorRepository = jugadorRepository;
    }

    @Override
    public Partido crearPartido(String torneoId, String equipoLocalId, String equipoVisitanteId,
                                LocalDateTime fecha, String cancha) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado con id: " + torneoId));
        Equipo local = equipoRepository.findById(equipoLocalId)
                .orElseThrow(() -> new RuntimeException("Equipo local no encontrado con id: " + equipoLocalId));
        Equipo visitante = equipoRepository.findById(equipoVisitanteId)
                .orElseThrow(() -> new RuntimeException("Equipo visitante no encontrado con id: " + equipoVisitanteId));

        if (local.getId().equals(visitante.getId()))
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");

        Partido partido = new Partido();
        partido.setTorneo(torneo);
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.setFecha(fecha);
        partido.setCancha(cancha);
        Partido saved = partidoRepository.save(partido);
        log.info("Partido creado exitosamente");
        return saved;
    }

    @Override
    public Partido iniciarPartido(String partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.iniciar();
        log.info("Partido iniciado");
        return partidoRepository.save(partido);
    }

    @Override
    public Partido registrarResultado(String partidoId, int golesLocal, int golesVisitante) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.registrarResultado(golesLocal, golesVisitante);
        log.info("Resultado registrado");
        return partidoRepository.save(partido);
    }

    @Override
    public Partido finalizarPartido(String partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.finalizar();
        log.info("Partido finalizado");
        return partidoRepository.save(partido);
    }

    @Override
    public Partido registrarGoleador(String partidoId, String jugadorId, int minuto) {
        Partido partido = getPartidoOrThrow(partidoId);
        validarPartidoEnCurso(partido);

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + jugadorId));

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
        return partidoRepository.save(partido);
    }

    @Override
    public Partido registrarSancion(String partidoId, String jugadorId, Sancion.TipoSancion tipoSancion, String descripcion) {
        Partido partido = getPartidoOrThrow(partidoId);
        validarPartidoEnCurso(partido);

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + jugadorId));

        Sancion sancion = new Sancion();
        sancion.setId(IdGeneratorUtil.generarId());
        sancion.setJugador(jugador);
        sancion.setTipoSancion(tipoSancion);
        sancion.setDescripcion(descripcion);
        partido.getSanciones().add(sancion);
        jugador.agregarSancion(sancion);

        log.info("Sanción registrada");
        return partidoRepository.save(partido);
    }

    @Override
    public Partido consultarPartido(String partidoId) {
        return getPartidoOrThrow(partidoId);
    }

    @Override
    public List<Partido> consultarPartidosPorTorneo(String torneoId) {
        return partidoRepository.findByTorneoId(torneoId);
    }

    @Override
    public List<Partido> consultarPartidosPorEquipo(String equipoId) {
        return partidoRepository.findByEquipoLocalIdOrEquipoVisitanteId(equipoId, equipoId);
    }

    private void validarPartidoEnCurso(Partido partido) {
        if (partido.getEstado() != Partido.PartidoEstado.EN_CURSO)
            throw new IllegalStateException("El partido no esta EN_CURSO.");
    }

    private Partido getPartidoOrThrow(String id) {
        return partidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado con id: " + id));
    }
}