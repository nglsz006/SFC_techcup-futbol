package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.repository.AlineacionRepository;
import edu.dosw.project.SFC_TechUp_Futbol.repository.AlineacionRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.validators.ValidacionStrategy;

import java.util.List;
import java.util.Map;

public class AlineacionService extends ObserverPattern.Subject {
    private AlineacionRepository repository;
    private ValidacionStrategy.Validacion validador;

    public AlineacionService() {
        this.repository = new AlineacionRepositoryImpl();
        this.validador = new ValidacionStrategy.ValidacionAlineacion();
    }

    public Alineacion crear(Alineacion alineacion, Map<String, Object> datos) {
        validador.validar(datos);
        Alineacion saved = repository.save(alineacion);
        notificar("ALINEACION_CREADA", Map.of("id", saved.getId(), "equipoId", saved.getEquipoId()));
        return saved;
    }

    public Alineacion obtener(int id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Alineacion no encontrada con id: " + id));
    }

    public List<Alineacion> listar() {
        return repository.findAll();
    }
}
