package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.repository.AlineacionRepository;
import edu.dosw.project.SFC_TechUp_Futbol.repository.AlineacionRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.validators.Validacion;
import edu.dosw.project.SFC_TechUp_Futbol.validators.ValidacionAlineacion;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AlineacionService extends Subject {
    private static final Logger log = Logger.getLogger(AlineacionService.class.getName());
    private AlineacionRepository repository;
    private Validacion validador;

    public AlineacionService() {
        this.repository = new AlineacionRepositoryImpl();
        this.validador = new ValidacionAlineacion();
    }

    public Alineacion crear(Alineacion alineacion, Map<String, Object> datos) {
        validador.validar(datos);
        Alineacion saved = repository.save(alineacion);
        log.info("Alineacion creada para equipo: " + saved.getEquipoId());
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
