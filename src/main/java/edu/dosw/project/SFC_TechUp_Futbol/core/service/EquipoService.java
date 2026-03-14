package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.EquipoRepository;

import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class EquipoService extends Subject {
    private final EquipoRepository repository;

    public EquipoService(EquipoRepository repository) {
        this.repository = repository;
    }

    public Equipo crear(Equipo equipo, Map<String, Object> datos) {
        Equipo saved = repository.save(equipo);
        notificar("EQUIPO_CREADO", Map.of("id", saved.getId(), "nombre", saved.getNombre()));
        return saved;
    }

    public Equipo obtener(int id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));
    }

    public List<Equipo> listar() {
        return repository.findAll();
    }

    public Equipo agregarJugador(int equipoId, int jugadorId) {
        Equipo equipo = obtener(equipoId);
        equipo.agregarJugador(jugadorId);
        notificar("JUGADOR_AGREGADO", Map.of("equipoId", equipoId, "jugadorId", jugadorId));
        return equipo;
    }
}
