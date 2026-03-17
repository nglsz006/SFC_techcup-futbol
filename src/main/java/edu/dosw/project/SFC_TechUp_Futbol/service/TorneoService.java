package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.repository.TorneoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.repository.TorneoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.validators.Validacion;
import edu.dosw.project.SFC_TechUp_Futbol.validators.ValidacionTorneo;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TorneoService extends Subject {
    private static final Logger log = Logger.getLogger(TorneoService.class.getName());
    private TorneoRepository repository;

    public TorneoService(TorneoRepository repository) {
        this.repository = repository;
    }

    public Torneo crear(Torneo torneo, Map<String, Object> datos) {
        Torneo saved = repository.save(torneo);
        log.info("Torneo creado: " + saved.getNombre());
        notificar("TORNEO_CREADO", Map.of("id", saved.getId(), "nombre", saved.getNombre()));
        return saved;
    }

    public Torneo obtener(int id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado con id: " + id));
    }

    public List<Torneo> listar() {
        return repository.findAll();
    }

    public Torneo iniciar(int id) {
        Torneo torneo = obtener(id);
        torneo.iniciar();
        log.info("Torneo iniciado: " + id);
        notificar("TORNEO_INICIADO", Map.of("id", id));
        return torneo;
    }

    public Torneo finalizar(int id) {
        Torneo torneo = obtener(id);
        torneo.finalizar();
        log.info("Torneo finalizado: " + id);
        notificar("TORNEO_FINALIZADO", Map.of("id", id));
        return torneo;
    }
}
