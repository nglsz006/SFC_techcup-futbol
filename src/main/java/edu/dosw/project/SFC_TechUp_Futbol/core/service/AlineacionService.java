package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.Validacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.ValidacionAlineacion;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.AlineacionMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AlineacionJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class AlineacionService extends Subject {

    private static final Logger log = Logger.getLogger(AlineacionService.class.getName());

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    private final AlineacionJpaRepository repository;
    private final AlineacionMapper mapper;
    private final Validacion validador;

    public AlineacionService(AlineacionJpaRepository repository, AlineacionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.validador = new ValidacionAlineacion();
    }

    public Alineacion crear(Alineacion alineacion, Map<String, Object> datos) {
        validador.validar(datos);
        if (alineacion.getId() == null) alineacion.setId(IdGeneratorUtil.generarId());
        Alineacion saved = mapper.toDomain(repository.save(mapper.toEntity(alineacion)));
        log.info("Alineacion creada para equipo: " + sanitize(saved.getEquipoId()));
        notificar("ALINEACION_CREADA", Map.of("id", saved.getId(), "equipoId", saved.getEquipoId()));
        return saved;
    }

    public Alineacion obtener(String id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new IllegalArgumentException("Alineacion no encontrada con id: " + id));
    }

    public List<Alineacion> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
