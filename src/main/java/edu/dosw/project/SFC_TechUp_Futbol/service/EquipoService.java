package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.repository.EquipoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.validators.Validacion;
import edu.dosw.project.SFC_TechUp_Futbol.validators.ValidacionEquipo;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class EquipoService extends Subject {
    private static final Logger log = Logger.getLogger(EquipoService.class.getName());
    private final EquipoRepository repository;

    public EquipoService(EquipoRepository repository) {
        this.repository = repository;
    }

    public Equipo crear(Equipo equipo, Map<String, Object> datos) {
        Equipo saved = repository.save(equipo);
        log.info("Equipo creado: " + saved.getNombre());
        notificar("EQUIPO_CREADO", Map.of("id", saved.getId(), "nombre", saved.getNombre()));
        return saved;
    }

    public Equipo obtener(int id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado con id: " + id));
    }

    public List<Equipo> listar() {
        return repository.findAll();
    }

    public Equipo agregarJugador(int equipoId, int jugadorId) {
        Equipo equipo = obtener(equipoId);
        equipo.agregarJugador(jugadorId);
        log.info("Jugador " + jugadorId + " agregado al equipo " + equipoId);
        notificar("JUGADOR_AGREGADO", Map.of("equipoId", equipoId, "jugadorId", jugadorId));
        return equipo;
    }
}
