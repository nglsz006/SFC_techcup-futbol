package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.TorneoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.PartidoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.TorneoJpaRepository;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@org.springframework.stereotype.Service
public class TorneoService extends Subject {

    private static final Logger log = Logger.getLogger(TorneoService.class.getName());

    private final TorneoJpaRepository repository;
    private final TorneoMapper mapper;
    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private PartidoJpaRepository partidoRepository;

    public TorneoService(TorneoJpaRepository repository, TorneoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    public Torneo crear(Torneo torneo, Map<String, Object> datos) {
        if (torneo.getNombre() == null || torneo.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del torneo es obligatorio.");
        if (torneo.getCantidadEquipos() < 2)
            throw new IllegalArgumentException("Debe haber al menos 2 equipos.");
        if (torneo.getCosto() < 0)
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        if (torneo.getFechaInicio() != null && torneo.getFechaFin() != null
                && !torneo.getFechaInicio().isBefore(torneo.getFechaFin()))
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        if (torneo.getId() == null) torneo.setId(IdGeneratorUtil.generarId());
        Torneo saved = mapper.toDomain(repository.save(mapper.toEntity(torneo)));
        log.info("Torneo creado");
        notificar("TORNEO_CREADO", Map.of("id", saved.getId(), "nombre", saved.getNombre()));
        return saved;
    }

    public Torneo obtener(String id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));
    }

    public List<Torneo> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    public Torneo iniciar(String id) {
        Torneo torneo = obtener(id);
        if (torneo.getEstado() == Torneo.EstadoTorneo.EN_CURSO)
            throw new IllegalStateException("El torneo ya está en curso.");
        if (torneo.getEstado() == Torneo.EstadoTorneo.FINALIZADO)
            throw new IllegalStateException("No se puede iniciar un torneo que ya finalizó.");
        torneo.iniciar();
        log.info("Torneo iniciado");
        notificar("TORNEO_INICIADO", Map.of("id", id));
        return mapper.toDomain(repository.save(mapper.toEntity(torneo)));
    }

    public Torneo finalizar(String id) {
        Torneo torneo = obtener(id);
        if (torneo.getEstado() == Torneo.EstadoTorneo.CREADO)
            throw new IllegalStateException("No se puede finalizar un torneo que aún no ha iniciado.");
        if (torneo.getEstado() == Torneo.EstadoTorneo.FINALIZADO)
            throw new IllegalStateException("El torneo ya está finalizado.");
        torneo.finalizar();
        log.info("Torneo finalizado");
        notificar("TORNEO_FINALIZADO", Map.of("id", id));
        return mapper.toDomain(repository.save(mapper.toEntity(torneo)));
    }

    public boolean puedeInscribirEquipos(String id) {
        return obtener(id).getEstadoObj().puedeInscribirEquipos();
    }

    public void eliminar(String id) {
        Torneo torneo = obtener(id);
        if (torneo.getEstado() == Torneo.EstadoTorneo.EN_CURSO)
            throw new IllegalStateException("No se puede eliminar un torneo que está en curso.");
        if (partidoRepository != null) {
            boolean tienePartidosEnCurso = partidoRepository.findByTorneoId(id).stream()
                .anyMatch(p -> p.getEstado() == Partido.PartidoEstado.EN_CURSO);
            if (tienePartidosEnCurso)
                throw new IllegalStateException("No se puede eliminar un torneo con partidos en curso.");
        }
        repository.deleteById(id);
        log.info("Torneo eliminado con id: " + sanitize(id));
    }

    public Torneo configurar(String id, String reglamento, String canchas, String horarios,
                             String sanciones, java.time.LocalDateTime cierreInscripciones) {
        Torneo torneo = obtener(id);

        if (torneo.getEstado() == Torneo.EstadoTorneo.EN_CURSO) {
            throw new IllegalStateException("No se puede configurar un torneo que ya está en progreso.");
        }

        if (torneo.getEstado() == Torneo.EstadoTorneo.FINALIZADO) {
            throw new IllegalStateException("No se puede configurar un torneo que ya finalizó.");
        }

        if (reglamento != null && !reglamento.isBlank()) torneo.setReglamento(reglamento);
        if (canchas != null && !canchas.isBlank()) torneo.setCanchas(canchas);
        if (horarios != null && !horarios.isBlank()) torneo.setHorarios(horarios);
        if (sanciones != null && !sanciones.isBlank()) torneo.setSanciones(sanciones);

        if (cierreInscripciones != null) {
            if (torneo.getFechaInicio() != null && !cierreInscripciones.isBefore(torneo.getFechaInicio())) {
                throw new IllegalArgumentException("El cierre de inscripciones debe ser anterior a la fecha de inicio del torneo.");
            }
            torneo.setCierreInscripciones(cierreInscripciones);
        }

        log.info("Torneo configurado");
        return mapper.toDomain(repository.save(mapper.toEntity(torneo)));
    }
}
