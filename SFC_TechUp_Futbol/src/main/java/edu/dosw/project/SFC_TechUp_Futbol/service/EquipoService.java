package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.repository.EquipoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.repository.EquipoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.validators.ValidacionStrategy;

import java.util.List;
import java.util.Map;

public class EquipoService extends ObserverPattern.Subject {
    private EquipoRepository repository;
    private ValidacionStrategy.Validacion validador;

    public EquipoService() {
        this.repository = new EquipoRepositoryImpl();
        this.validador = new ValidacionStrategy.ValidacionEquipo();
    }

    public Equipo crear(Equipo equipo, Map<String, Object> datos) {
        validador.validar(datos);
        Equipo saved = repository.save(equipo);
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
        notificar("JUGADOR_AGREGADO", Map.of("equipoId", equipoId, "jugadorId", jugadorId));
        return equipo;
    }
}
