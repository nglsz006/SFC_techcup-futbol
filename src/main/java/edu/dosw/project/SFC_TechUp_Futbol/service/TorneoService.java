package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.repository.TorneoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.repository.TorneoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.validators.ValidacionStrategy;

import java.util.List;
import java.util.Map;

public class TorneoService extends ObserverPattern.Subject {
    private TorneoRepository repository;
    private ValidacionStrategy.Validacion validador;

    public TorneoService() {
        this.repository = new TorneoRepositoryImpl();
        this.validador = new ValidacionStrategy.ValidacionTorneo();
    }

    public Torneo crear(Torneo torneo, Map<String, Object> datos) {
        validador.validar(datos);
        Torneo saved = repository.save(torneo);
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
