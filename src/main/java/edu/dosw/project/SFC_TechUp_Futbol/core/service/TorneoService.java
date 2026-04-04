package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.TorneoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.TorneoJpaRepository;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@org.springframework.stereotype.Service
public class TorneoService extends Subject {

    private static final Logger log = Logger.getLogger(TorneoService.class.getName());

    private final TorneoJpaRepository repository;
    private final TorneoMapper mapper;

    public TorneoService(TorneoJpaRepository repository, TorneoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Torneo crear(Torneo torneo, Map<String, Object> datos) {
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
        torneo.iniciar();
        log.info("Torneo iniciado");
        notificar("TORNEO_INICIADO", Map.of("id", id));
        return mapper.toDomain(repository.save(mapper.toEntity(torneo)));
    }

    public Torneo finalizar(String id) {
        Torneo torneo = obtener(id);
        torneo.finalizar();
        log.info("Torneo finalizado");
        notificar("TORNEO_FINALIZADO", Map.of("id", id));
        return mapper.toDomain(repository.save(mapper.toEntity(torneo)));
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
