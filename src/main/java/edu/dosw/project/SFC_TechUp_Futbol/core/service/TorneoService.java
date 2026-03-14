package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.TorneoRepository;

import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class TorneoService extends Subject {
    private final TorneoRepository repository;

    public TorneoService(TorneoRepository repository) {
        this.repository = repository;
    }

    public Torneo crear(Torneo torneo, Map<String, Object> datos) {
        Torneo saved = repository.save(torneo);
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
        notificar("TORNEO_INICIADO", Map.of("id", id));
        return torneo;
    }

    public Torneo finalizar(int id) {
        Torneo torneo = obtener(id);
        torneo.finalizar();
        notificar("TORNEO_FINALIZADO", Map.of("id", id));
        return torneo;
    }
}
