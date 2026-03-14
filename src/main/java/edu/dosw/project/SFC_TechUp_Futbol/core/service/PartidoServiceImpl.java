package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Service
public class PartidoServiceImpl implements PartidoService {

    private final PartidoRepository partidoRepository;
    private final TorneoRepository torneoRepository;
    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;
    private final GolRepository golRepository;
    private final TarjetaRepository tarjetaRepository;

    public PartidoServiceImpl(PartidoRepository partidoRepository,
                              TorneoRepository torneoRepository,
                              EquipoRepository equipoRepository,
                              JugadorRepository jugadorRepository,
                              GolRepository golRepository,
                              TarjetaRepository tarjetaRepository) {
        this.partidoRepository = partidoRepository;
        this.torneoRepository = torneoRepository;
        this.equipoRepository = equipoRepository;
        this.jugadorRepository = jugadorRepository;
        this.golRepository = golRepository;
        this.tarjetaRepository = tarjetaRepository;
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

        if (local.getId() == visitante.getId()) {
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");
        }

        Partido partido = new Partido();
        partido.setTorneo(torneo);
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.setFecha(fecha);
        partido.setCancha(cancha);
        return partidoRepository.save(partido);
    }

    @Override
    public Partido iniciarPartido(Long partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.iniciar();
        return partidoRepository.save(partido);
    }

    @Override
    public Partido registrarResultado(Long partidoId, int golesLocal, int golesVisitante) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.registrarResultado(golesLocal, golesVisitante);
        return partidoRepository.save(partido);
    }

    @Override
    public Partido finalizarPartido(Long partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.finalizar();
        return partidoRepository.save(partido);
    }

    @Override
    public Partido registrarGoleador(Long partidoId, Long jugadorId, int minuto) {
        Partido partido = getPartidoOrThrow(partidoId);

        validarPartidoEnCurso(partido);

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + jugadorId));

        Gol gol = new Gol();
        gol.setPartido(partido);
        gol.setJugador(jugador);
        gol.setMinuto(minuto);
        golRepository.save(gol);

        boolean esLocal = partido.getEquipoLocal().getId() == jugador.getEquipo();
        if (esLocal) {
            partido.setMarcadorLocal(partido.getMarcadorLocal() + 1);
        } else {
            partido.setMarcadorVisitante(partido.getMarcadorVisitante() + 1);
        }
        return partidoRepository.save(partido);
    }

    @Override
    public Partido registrarTarjeta(Long partidoId, Long jugadorId, Tarjeta.TipoTarjeta tipo, int minuto) {
        Partido partido = getPartidoOrThrow(partidoId);

        validarPartidoEnCurso(partido);

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + jugadorId));

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setPartido(partido);
        tarjeta.setJugador(jugador);
        tarjeta.setTipo(tipo);
        tarjeta.setMinuto(minuto);
        tarjetaRepository.save(tarjeta);

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
        if (partido.getEstado() != PartidoEstado.EN_CURSO)
            throw new IllegalStateException("El partido no esta EN_CURSO.");
    }

    private Partido getPartidoOrThrow(Long id) {
        return partidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado con id: " + id));
    }
}

