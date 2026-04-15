package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.Validacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.ValidacionAlineacion;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.AlineacionMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PartidoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AlineacionJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.PartidoJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class AlineacionService extends Subject {

    private static final Logger log = Logger.getLogger(AlineacionService.class.getName());

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    private final AlineacionJpaRepository repository;
    private final AlineacionMapper mapper;
    private final EquipoJpaRepository equipoRepository;
    private final EquipoMapper equipoMapper;
    private final PartidoJpaRepository partidoRepository;
    private final PartidoMapper partidoMapper;
    private final Validacion validador;

    public AlineacionService(AlineacionJpaRepository repository, AlineacionMapper mapper,
                             EquipoJpaRepository equipoRepository, EquipoMapper equipoMapper,
                             PartidoJpaRepository partidoRepository, PartidoMapper partidoMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.equipoRepository = equipoRepository;
        this.equipoMapper = equipoMapper;
        this.partidoRepository = partidoRepository;
        this.partidoMapper = partidoMapper;
        this.validador = new ValidacionAlineacion();
    }

    public Alineacion registrar(String equipoId, String partidoId, Alineacion.Formacion formacion,
                                List<String> titulares, List<String> reservas) {
        equipoRepository.findById(equipoId)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado."));
        partidoRepository.findById(partidoId)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado."));

        Map<String, Object> datos = new java.util.HashMap<>();
        datos.put("formacion", formacion != null ? formacion.getValor() : null);
        datos.put("titulares", titulares);
        if (reservas != null) datos.put("reservas", reservas);
        validador.validar(datos);

        Equipo equipo = equipoMapper.toDomain(equipoRepository.findById(equipoId)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado.")));
        validarJugadoresDelEquipo(equipo, titulares, reservas);

        Alineacion alineacion = new Alineacion();
        alineacion.setId(IdGeneratorUtil.generarId());
        alineacion.setEquipoId(equipoId);
        alineacion.setPartidoId(partidoId);
        alineacion.setFormacion(formacion);
        alineacion.setTitulares(titulares);
        alineacion.setReservas(reservas != null ? reservas : List.of());

        Alineacion saved = mapper.toDomain(repository.save(mapper.toEntity(alineacion)));
        log.info("Alineacion registrada para equipo: " + sanitize(equipoId) + " partido: " + sanitize(partidoId));
        notificar("ALINEACION_CREADA", Map.of("id", saved.getId(), "equipoId", saved.getEquipoId()));
        return saved;
    }

    public Alineacion crear(Alineacion alineacion, Map<String, Object> datos) {
        validador.validar(datos);
        if (alineacion.getId() == null) alineacion.setId(IdGeneratorUtil.generarId());
        Alineacion saved = mapper.toDomain(repository.save(mapper.toEntity(alineacion)));
        log.info("Alineacion creada para equipo: " + sanitize(saved.getEquipoId()));
        notificar("ALINEACION_CREADA", Map.of("id", saved.getId(), "equipoId", saved.getEquipoId()));
        return saved;
    }

    public Alineacion obtener(String id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new IllegalArgumentException("Alineacion no encontrada con id: " + id));
    }

    public Alineacion obtenerPorPartidoYEquipo(String partidoId, String equipoId) {
        return repository.findByPartidoIdAndEquipoId(partidoId, equipoId)
                .map(mapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Alineacion no encontrada para ese partido y equipo."));
    }

    public Alineacion obtenerRival(String partidoId, String equipoId) {
        List<Alineacion> alineaciones = repository.findByPartidoId(partidoId)
                .stream().map(mapper::toDomain).toList();
        return alineaciones.stream()
                .filter(a -> !a.getEquipoId().equals(equipoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No hay alineacion rival registrada para este partido."));
    }

    public List<Alineacion> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    private void validarJugadoresDelEquipo(Equipo equipo, List<String> titulares, List<String> reservas) {
        List<String> jugadoresEquipo = equipo.getJugadores();
        if (jugadoresEquipo == null || jugadoresEquipo.isEmpty()) return;

        for (String jugadorId : titulares) {
            if (!jugadoresEquipo.contains(jugadorId))
                throw new IllegalArgumentException("El jugador " + jugadorId + " no pertenece al equipo.");
        }
        if (reservas != null) {
            for (String jugadorId : reservas) {
                if (!jugadoresEquipo.contains(jugadorId))
                    throw new IllegalArgumentException("El jugador " + jugadorId + " no pertenece al equipo.");
            }
        }
    }
}
