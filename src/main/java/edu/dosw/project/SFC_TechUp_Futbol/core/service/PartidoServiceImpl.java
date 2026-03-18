package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;

import java.time.LocalDateTime;
import java.util.List;
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
    public Partido crearPartido(Long torneoId, Long equipoLocalId, Long equipoVisitanteId,
                                LocalDateTime fecha, String cancha) {
        Torneo torneo = torneoRepository.findById(torneoId.intValue())
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado con id: " + torneoId));
        Equipo local = equipoRepository.findById(equipoLocalId.intValue())
                .orElseThrow(() -> new RuntimeException("Equipo local no encontrado con id: " + equipoLocalId));
        Equipo visitante = equipoRepository.findById(equipoVisitanteId.intValue())
                .orElseThrow(() -> new RuntimeException("Equipo visitante no encontrado con id: " + equipoVisitanteId));

        if (local.getId() == visitante.getId())
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");

        Partido partido = new Partido();
        partido.setTorneo(torneo);
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.setFecha(fecha);
        partido.setCancha(cancha);
        Partido saved = partidoRepository.save(partido);
        log.info("Partido creado: " + local.getNombre() + " vs " + visitante.getNombre());
        return saved;
    }

    @Override
    public Partido iniciarPartido(Long partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.iniciar();
        log.info("Partido iniciado: " + partidoId);
        return partidoRepository.save(partido);
    }

    @Override
    public Partido registrarResultado(Long partidoId, int golesLocal, int golesVisitante) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.registrarResultado(golesLocal, golesVisitante);
        log.info("Resultado registrado partido " + partidoId + ": " + golesLocal + "-" + golesVisitante);
        return partidoRepository.save(partido);
    }

    @Override
    public Partido finalizarPartido(Long partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.finalizar();
        log.info("Partido finalizado: " + partidoId);
        return partidoRepository.save(partido);
    }

    @Override
    public Partido registrarGoleador(Long partidoId, Long jugadorId, int minuto) {
        Partido partido = getPartidoOrThrow(partidoId);
        validarPartidoEnCurso(partido);

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + jugadorId));

        Partido.Gol gol = new Partido.Gol();
        gol.setJugador(jugador);
        gol.setMinuto(minuto);
        partido.getGoles().add(gol);

        boolean esLocal = jugador.getEquipo() != null && partido.getEquipoLocal().getId() == jugador.getEquipo();
        if (esLocal) {
            partido.setMarcadorLocal(partido.getMarcadorLocal() + 1);
        } else {
            partido.setMarcadorVisitante(partido.getMarcadorVisitante() + 1);
        }
        log.info("Gol registrado: jugador " + jugadorId + " minuto " + minuto);
        return partidoRepository.save(partido);
    }

    @Override
    public Partido registrarTarjeta(Long partidoId, Long jugadorId, Partido.Tarjeta.TipoTarjeta tipo, int minuto) {
        Partido partido = getPartidoOrThrow(partidoId);
        validarPartidoEnCurso(partido);

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + jugadorId));

        Partido.Tarjeta tarjeta = new Partido.Tarjeta();
        tarjeta.setJugador(jugador);
        tarjeta.setTipo(tipo);
        tarjeta.setMinuto(minuto);
        partido.getTarjetas().add(tarjeta);

        log.info("Tarjeta " + tipo + " registrada: jugador " + jugadorId + " minuto " + minuto);
        return partidoRepository.save(partido);
    }

    @Override
    public Partido consultarPartido(Long partidoId) {
        return getPartidoOrThrow(partidoId);
    }

    @Override
    public List<Partido> consultarPartidosPorTorneo(Long torneoId) {
        return partidoRepository.findByTorneoId(torneoId);
    }

    @Override
    public List<Partido> consultarPartidosPorEquipo(Long equipoId) {
        return partidoRepository.findByEquipoLocalIdOrEquipoVisitanteId(equipoId, equipoId);
    }

    private void validarPartidoEnCurso(Partido partido) {
        if (partido.getEstado() != Partido.PartidoEstado.EN_CURSO)
            throw new IllegalStateException("El partido no esta EN_CURSO.");
    }

    private Partido getPartidoOrThrow(Long id) {
        return partidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado con id: " + id));
    }
}