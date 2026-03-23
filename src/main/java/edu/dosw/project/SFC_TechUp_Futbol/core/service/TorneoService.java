package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.TorneoRepository;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@org.springframework.stereotype.Service
public class TorneoService extends Subject {

    private static final Logger log = Logger.getLogger(TorneoService.class.getName());

    private final TorneoRepository repository;

    public TorneoService(TorneoRepository repository) {
        this.repository = repository;
    }

    public Torneo crear(Torneo torneo, Map<String, Object> datos) {
        Torneo saved = repository.save(torneo);
        log.info("Torneo creado");
        notificar("TORNEO_CREADO", Map.of("id", saved.getId(), "nombre", saved.getNombre()));
        return saved;
    }

    public Torneo obtener(int id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));
    }

    public List<Torneo> listar() {
        return repository.findAll();
    }

    public Torneo iniciar(int id) {
        Torneo torneo = obtener(id);
        torneo.iniciar();
        log.info("Torneo iniciado");
        notificar("TORNEO_INICIADO", Map.of("id", id));
        return torneo;
    }

    public Torneo finalizar(int id) {
        Torneo torneo = obtener(id);
        torneo.finalizar();
        log.info("Torneo finalizado");
        notificar("TORNEO_FINALIZADO", Map.of("id", id));
        return torneo;
    }

    public Torneo configurar(int id, String reglamento, String canchas, String horarios,
                             String sanciones, java.time.LocalDateTime cierreInscripciones) {
        Torneo torneo = obtener(id);
        if (reglamento != null && !reglamento.isBlank()) torneo.setReglamento(reglamento);
        if (canchas != null && !canchas.isBlank()) torneo.setCanchas(canchas);
        if (horarios != null && !horarios.isBlank()) torneo.setHorarios(horarios);
        if (sanciones != null && !sanciones.isBlank()) torneo.setSanciones(sanciones);
        if (cierreInscripciones != null) {
            if (torneo.getFechaInicio() != null && !cierreInscripciones.isBefore(torneo.getFechaInicio()))
                throw new IllegalArgumentException("El cierre de inscripciones debe ser anterior a la fecha de inicio del torneo.");
            torneo.setCierreInscripciones(cierreInscripciones);
        }
        log.info("Torneo configurado");
        return repository.save(torneo);
    }
}
