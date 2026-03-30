package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.UsuarioValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrganizadorService {

    private final OrganizadorRepository organizadorRepository;
    private final TorneoService torneoService;
    private final UsuarioValidator usuarioValidator = new UsuarioValidator();

    public OrganizadorService(OrganizadorRepository organizadorRepository, TorneoService torneoService) {
        this.organizadorRepository = organizadorRepository;
        this.torneoService = torneoService;
    }

    public Organizador save(Organizador organizador) {
        return organizadorRepository.save(organizador);
    }

    public Torneo crearTorneo(String organizadorId, Torneo torneo) {
        Organizador organizador = getOrThrow(organizadorId);
        if (!usuarioValidator.nombreValido(torneo.getNombre())) throw new IllegalArgumentException("nombre de torneo no valido");
        Torneo creado = torneoService.crear(torneo, Map.of());
        organizador.setCurrentTournament(creado);
        organizadorRepository.save(organizador);
        return creado;
    }

    public Torneo iniciarTorneo(String organizadorId) {
        Organizador organizador = getOrThrow(organizadorId);
        if (organizador.getCurrentTournament() == null) throw new IllegalStateException("no tiene torneo activo");
        return torneoService.iniciar(organizador.getCurrentTournament().getId());
    }

    public Torneo finalizarTorneo(String organizadorId) {
        Organizador organizador = getOrThrow(organizadorId);
        if (organizador.getCurrentTournament() == null) throw new IllegalStateException("no tiene torneo activo");
        return torneoService.finalizar(organizador.getCurrentTournament().getId());
    }

    public List<Organizador> getOrganizadores() {
        return organizadorRepository.findAll();
    }

    private Organizador getOrThrow(String id) {
        return organizadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("organizador no encontrado"));
    }
}
