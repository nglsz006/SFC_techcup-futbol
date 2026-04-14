package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
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
        log.info("Partido finalizado");
        return partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido)));
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

        List<Equipo> equipos = equipoRepository.findAll().stream()
                .map(equipoMapper::toDomain).collect(Collectors.toList());
        if (equipos.size() < 2)
            throw new IllegalStateException("Se necesitan al menos 2 equipos para generar llaves.");

        Collections.shuffle(equipos);
        List<Partido> partidos = new ArrayList<>();
        String[] fases = {"FASE_INICIAL", "CUARTOS", "SEMIFINAL", "FINAL"};
        int fase = 0;

        while (equipos.size() > 1) {
            List<Equipo> siguienteRonda = new ArrayList<>();
            String nombreFase = fase < fases.length ? fases[fase] : "FASE_" + fase;
            for (int i = 0; i + 1 < equipos.size(); i += 2) {
                Partido partido = new Partido();
                partido.setId(IdGeneratorUtil.generarId());
                partido.setTorneo(torneo);
                partido.setEquipoLocal(equipos.get(i));
                partido.setEquipoVisitante(equipos.get(i + 1));
                partido.setCancha(nombreFase);
                partido.setFecha(LocalDateTime.now().plusDays(fase + 1));
                partidos.add(partidoMapper.toDomain(partidoRepository.save(partidoMapper.toEntity(partido))));
                siguienteRonda.add(equipos.get(i));
            }
            if (equipos.size() % 2 != 0) siguienteRonda.add(equipos.get(equipos.size() - 1));
            equipos = siguienteRonda;
            fase++;
            if (equipos.size() == 1) break;
        }
        log.info("Llaves generadas para torneo: " + torneoId);
        return partidos;
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