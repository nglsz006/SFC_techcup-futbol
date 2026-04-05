package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.ValidacionEquipo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@org.springframework.stereotype.Service
public class EquipoService extends Subject {

    private static final Logger log = Logger.getLogger(EquipoService.class.getName());

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    private final EquipoJpaRepository repository;
    private final EquipoMapper mapper;

    public EquipoService(EquipoJpaRepository repository, EquipoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Equipo crear(Equipo equipo, Map<String, Object> datos) {
        new ValidacionEquipo().validar(datos);
        if (equipo.getId() == null) equipo.setId(IdGeneratorUtil.generarId());
        Equipo saved = mapper.toDomain(repository.save(mapper.toEntity(equipo)));
        log.info("Equipo creado con id: " + sanitize(saved.getId()));
        notificar("EQUIPO_CREADO", Map.of("id", saved.getId(), "nombre", saved.getNombre()));
        return saved;
    }

    public Equipo obtener(String id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));
    }

    public List<Equipo> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    public Equipo agregarJugador(String equipoId, String jugadorId) {
        Equipo equipo = obtener(equipoId);
        equipo.agregarJugador(jugadorId);
        log.info("Jugador agregado al equipo");
        notificar("JUGADOR_AGREGADO", Map.of("equipoId", equipoId, "jugadorId", jugadorId));
        return mapper.toDomain(repository.save(mapper.toEntity(equipo)));
    }

    public Map<String, Object> validarComposicion(String equipoId) {
        Equipo equipo = obtener(equipoId);
        int total = equipo.getJugadores().size();
        boolean valido = total >= 7 && total <= 12;
        Map<String, Object> resultado = new java.util.LinkedHashMap<>();
        resultado.put("equipoId", equipoId);
        resultado.put("nombre", equipo.getNombre());
        resultado.put("totalJugadores", total);
        resultado.put("valido", valido);
        if (total < 7) resultado.put("error", "El equipo necesita al menos " + (7 - total) + " jugador(es) más.");
        if (total > 12) resultado.put("error", "El equipo excede el máximo de 12 jugadores.");
        return resultado;
    }
}
