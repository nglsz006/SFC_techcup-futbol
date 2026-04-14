package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PartidoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PartidoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.TorneoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.PartidoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.TorneoJpaRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    private final PartidoValidator partidoValidator;

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
        this.partidoValidator = new PartidoValidator();
    }

    @Override
    public Partido crearPartido(String torneoId, String equipoLocalId, String equipoVisitanteId,
                                LocalDateTime fecha, String cancha) {
        partidoValidator.validarCrearPartido(torneoId, equipoLocalId, equipoVisitanteId, fecha, cancha);
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

        if (torneo.getEstado() == Torneo.EstadoTorneo.FINALIZADO)
            throw new IllegalStateException("No se pueden crear partidos en un torneo finalizado.");

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
        partidoValidator.validarResultado(golesLocal, golesVisitante);
        partido.registrarResultado(golesLocal, golesVisitante);
        log.info("Resultado registrado");
        return partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido)));
    }

    @Override
    public Partido finalizarPartido(String partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        partido.finalizar();
        Partido saved = partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido)));
        if (partido.getFase() != null) avanzarGanador(partido);
        log.info("Partido finalizado");
        return saved;
    }

    @Override
    public Partido registrarGoleador(String partidoId, String jugadorId, int minuto) {
        partidoValidator.validarGoleador(jugadorId, minuto);
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
    public Partido registrarTarjeta(String partidoId, String jugadorId, Partido.Tarjeta.TipoTarjeta tipo, int minuto) {
        partidoValidator.validarTarjeta(jugadorId, tipo, minuto);
        Partido partido = getPartidoOrThrow(partidoId);
        validarPartidoEnCurso(partido);

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .map(jugadorMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado."));

        Partido.Tarjeta tarjeta = new Partido.Tarjeta();
        tarjeta.setId(IdGeneratorUtil.generarId());
        tarjeta.setJugador(jugador);
        tarjeta.setTipo(tipo);
        tarjeta.setMinuto(minuto);
        partido.getTarjetas().add(tarjeta);

        log.info("Tarjeta registrada");
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
        partidoValidator.validarSancion(sancion);
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
    public Map<String, Object> consultarEventos(String partidoId) {
        Partido partido = getPartidoOrThrow(partidoId);
        Map<String, Object> eventos = new HashMap<>();
        eventos.put("goles", partido.getGoles());
        eventos.put("tarjetas", partido.getTarjetas());
        eventos.put("sanciones", partido.getSanciones());
        return eventos;
    }

    @Override
    public List<Partido> consultarPartidosPorTorneo(String torneoId) {
        return partidoRepository.findByTorneoId(torneoId).stream().map(partidoMapper::toDomain).toList();
    }

    @Override
    public List<Partido> consultarPartidosPorEquipo(String equipoId) {
        return partidoRepository.findByEquipoLocalIdOrEquipoVisitanteId(equipoId, equipoId).stream().map(partidoMapper::toDomain).toList();
    }

    @Override
    public List<Partido> consultarPartidosPorEstado(Partido.PartidoEstado estado) {
        return partidoRepository.findByEstado(estado).stream().map(partidoMapper::toDomain).toList();
    }

    @Override
    public List<Partido> generarLlaves(String torneoId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .map(torneoMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado."));
        if (torneo.getEstado() == Torneo.EstadoTorneo.FINALIZADO)
            throw new IllegalStateException("No se pueden generar llaves en un torneo finalizado.");

        // Tomar top 8 de la tabla de posiciones
        List<Equipo> equipos = calcularTabla(torneoId);
        if (equipos.size() < 2)
            throw new IllegalStateException("Se necesitan al menos 2 equipos para generar llaves.");
        if (equipos.size() > 8) equipos = equipos.subList(0, 8);

        // Mezclar aleatoriamente para cuartos
        Collections.shuffle(equipos);
        List<Partido> partidos = new ArrayList<>();
        for (int i = 0; i + 1 < equipos.size(); i += 2) {
            Partido partido = new Partido();
            partido.setId(IdGeneratorUtil.generarId());
            partido.setTorneo(torneo);
            partido.setEquipoLocal(equipos.get(i));
            partido.setEquipoVisitante(equipos.get(i + 1));
            partido.setFase(Partido.Fase.CUARTOS);
            partido.setFecha(LocalDateTime.now().plusDays(1));
            partidos.add(partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido))));
        }
        log.info("Llaves de cuartos generadas para torneo: " + torneoId);
        return partidos;
    }

    private List<Equipo> calcularTabla(String torneoId) {
        List<PartidoEntity> partidos = partidoRepository.findByTorneoId(torneoId);
        Map<String, Integer> puntos = new LinkedHashMap<>();
        Map<String, Equipo> equiposMap = new LinkedHashMap<>();

        for (PartidoEntity p : partidos) {
            if (p.getEstado() != Partido.PartidoEstado.FINALIZADO) continue;
            Equipo local = equipoMapper.toDomain(p.getEquipoLocal());
            Equipo visitante = equipoMapper.toDomain(p.getEquipoVisitante());
            puntos.putIfAbsent(local.getId(), 0);
            puntos.putIfAbsent(visitante.getId(), 0);
            equiposMap.putIfAbsent(local.getId(), local);
            equiposMap.putIfAbsent(visitante.getId(), visitante);
            int gl = p.getMarcadorLocal(), gv = p.getMarcadorVisitante();
            if (gl > gv) puntos.merge(local.getId(), 3, Integer::sum);
            else if (gl < gv) puntos.merge(visitante.getId(), 3, Integer::sum);
            else { puntos.merge(local.getId(), 1, Integer::sum); puntos.merge(visitante.getId(), 1, Integer::sum); }
        }
        return puntos.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .map(e -> equiposMap.get(e.getKey()))
                .collect(Collectors.toList());
    }

    private void avanzarGanador(Partido partido) {
        if (partido.getTorneo() == null) return;
        String torneoId = partido.getTorneo().getId();
        Equipo ganador = partido.getMarcadorLocal() >= partido.getMarcadorVisitante()
                ? partido.getEquipoLocal() : partido.getEquipoVisitante();

        if (partido.getFase() == Partido.Fase.CUARTOS) {
            List<PartidoEntity> cuartos = partidoRepository.findByTorneoIdAndFase(torneoId, Partido.Fase.CUARTOS);
            long finalizados = cuartos.stream().filter(p -> p.getEstado() == Partido.PartidoEstado.FINALIZADO).count();
            // Cada 2 cuartos finalizados se crea una semifinal
            if (finalizados % 2 == 0) {
                List<PartidoEntity> semis = partidoRepository.findByTorneoIdAndFase(torneoId, Partido.Fase.SEMIFINAL);
                // Buscar el otro ganador del par
                int par = (int) ((finalizados / 2) - 1);
                PartidoEntity otroCuarto = cuartos.stream()
                        .filter(p -> p.getEstado() == Partido.PartidoEstado.FINALIZADO)
                        .filter(p -> !p.getId().equals(partido.getId()))
                        .skip(par)
                        .findFirst().orElse(null);
                if (otroCuarto != null) {
                    Equipo otroGanador = otroCuarto.getMarcadorLocal() >= otroCuarto.getMarcadorVisitante()
                            ? equipoMapper.toDomain(otroCuarto.getEquipoLocal())
                            : equipoMapper.toDomain(otroCuarto.getEquipoVisitante());
                    Torneo torneo = torneoRepository.findById(torneoId).map(torneoMapper::toDomain).orElse(null);
                    Partido semi = new Partido();
                    semi.setId(IdGeneratorUtil.generarId());
                    semi.setTorneo(torneo);
                    semi.setEquipoLocal(ganador);
                    semi.setEquipoVisitante(otroGanador);
                    semi.setFase(Partido.Fase.SEMIFINAL);
                    semi.setFecha(LocalDateTime.now().plusDays(2));
                    partidoRepository.save(partidoMapper.toEntity(semi));
                    log.info("Partido de semifinal creado");
                }
            }
        } else if (partido.getFase() == Partido.Fase.SEMIFINAL) {
            List<PartidoEntity> semis = partidoRepository.findByTorneoIdAndFase(torneoId, Partido.Fase.SEMIFINAL);
            long finalizados = semis.stream().filter(p -> p.getEstado() == Partido.PartidoEstado.FINALIZADO).count();
            if (finalizados == 2) {
                PartidoEntity otraSemi = semis.stream()
                        .filter(p -> p.getEstado() == Partido.PartidoEstado.FINALIZADO)
                        .filter(p -> !p.getId().equals(partido.getId()))
                        .findFirst().orElse(null);
                if (otraSemi != null) {
                    Equipo otroGanador = otraSemi.getMarcadorLocal() >= otraSemi.getMarcadorVisitante()
                            ? equipoMapper.toDomain(otraSemi.getEquipoLocal())
                            : equipoMapper.toDomain(otraSemi.getEquipoVisitante());
                    Torneo torneo = torneoRepository.findById(torneoId).map(torneoMapper::toDomain).orElse(null);
                    Partido final_ = new Partido();
                    final_.setId(IdGeneratorUtil.generarId());
                    final_.setTorneo(torneo);
                    final_.setEquipoLocal(ganador);
                    final_.setEquipoVisitante(otroGanador);
                    final_.setFase(Partido.Fase.FINAL);
                    final_.setFecha(LocalDateTime.now().plusDays(3));
                    partidoRepository.save(partidoMapper.toEntity(final_));
                    log.info("Partido de final creado");
                }
            }
        } else if (partido.getFase() == Partido.Fase.FINAL) {
            torneoRepository.findById(torneoId).ifPresent(t -> {
                t.setCampeonId(ganador.getId());
                torneoRepository.save(t);
                log.info("Campeon registrado: " + ganador.getId());
            });
        }
    }

    @Override
    public List<Map<String, Object>> maximosGoleadores(String torneoId) {
        List<Partido> partidos = consultarPartidosPorTorneo(torneoId);
        Map<String, Integer> golesPorJugador = new LinkedHashMap<>();
        Map<String, String> nombresPorJugador = new LinkedHashMap<>();

        for (Partido p : partidos) {
            for (Partido.Gol gol : p.getGoles()) {
                if (gol.getJugador() == null) continue;
                String jugadorId = gol.getJugador().getId();
                golesPorJugador.merge(jugadorId, 1, Integer::sum);
                nombresPorJugador.putIfAbsent(jugadorId, gol.getJugadorNombre());
            }
        }

        return golesPorJugador.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .map(e -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("jugadorId", e.getKey());
                    entry.put("nombre", nombresPorJugador.get(e.getKey()));
                    entry.put("goles", e.getValue());
                    return entry;
                })
                .collect(Collectors.toList());
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