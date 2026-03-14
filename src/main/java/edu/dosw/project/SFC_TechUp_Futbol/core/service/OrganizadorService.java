package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.UsuarioValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizadorService {

    private final List<Organizador> organizadores = new ArrayList<>();
    private final TorneoService torneoService;
    private final UsuarioValidator usuarioValidator = new UsuarioValidator();

    public OrganizadorService(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    public Torneo crearTorneo(Long organizadorId, Torneo torneo) {
        Organizador organizador = buscarOrganizadorPorId(organizadorId);
        if (organizador == null) throw new IllegalArgumentException("organizador no encontrado");
        if (!usuarioValidator.nombreValido(torneo.getNombre())) throw new IllegalArgumentException("nombre de torneo no valido");
        Torneo creado = torneoService.crear(torneo, java.util.Map.of());
        organizador.setCurrentTournament(creado);
        return creado;
    }

    public Torneo iniciarTorneo(Long organizadorId) {
        Organizador organizador = buscarOrganizadorPorId(organizadorId);
        if (organizador == null) throw new IllegalArgumentException("organizador no encontrado");
        if (organizador.getCurrentTournament() == null) throw new IllegalStateException("no tiene torneo activo");
        return torneoService.iniciar(organizador.getCurrentTournament().getId());
    }

    public Torneo finalizarTorneo(Long organizadorId) {
        Organizador organizador = buscarOrganizadorPorId(organizadorId);
        if (organizador == null) throw new IllegalArgumentException("organizador no encontrado");
        if (organizador.getCurrentTournament() == null) throw new IllegalStateException("no tiene torneo activo");
        return torneoService.finalizar(organizador.getCurrentTournament().getId());
    }

    public Organizador buscarOrganizadorPorId(Long id) {
        return organizadores.stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Organizador> getOrganizadores() {
        return organizadores;
    }
}
