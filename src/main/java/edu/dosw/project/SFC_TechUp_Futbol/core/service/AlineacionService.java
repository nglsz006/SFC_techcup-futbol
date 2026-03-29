package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AlineacionRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.Validacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.ValidacionAlineacion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class AlineacionService extends Subject {

    private static final Logger log = Logger.getLogger(AlineacionService.class.getName());

    private final AlineacionRepository repository;
    private final Validacion validador;

    public AlineacionService(AlineacionRepository repository) {
        this.repository = repository;
        this.validador = new ValidacionAlineacion();
    }

    public Alineacion crear(Alineacion alineacion, Map<String, Object> datos) {
        validador.validar(datos);
        Alineacion saved = repository.save(alineacion);
        log.info("Alineacion creada para equipo: " + saved.getEquipoId());
        notificar("ALINEACION_CREADA", Map.of("id", saved.getId(), "equipoId", saved.getEquipoId()));
        return saved;
    }

    public Alineacion obtener(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Alineacion no encontrada con id: " + id));
    }

    public List<Alineacion> listar() {
        return repository.findAll();
    }
}
