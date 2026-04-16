package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.UsuarioValidator;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.OrganizadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.OrganizadorJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrganizadorService {

    private final OrganizadorJpaRepository organizadorRepository;
    private final OrganizadorMapper mapper;
    private final TorneoService torneoService;
    private final UsuarioValidator usuarioValidator = new UsuarioValidator();

    public OrganizadorService(OrganizadorJpaRepository organizadorRepository, OrganizadorMapper mapper, TorneoService torneoService) {
        this.organizadorRepository = organizadorRepository;
        this.mapper = mapper;
        this.torneoService = torneoService;
    }

    public Organizador save(Organizador organizador) {
        if (organizador.getId() == null) organizador.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(organizadorRepository.save(mapper.toEntity(organizador)));
    }

    public Torneo crearTorneo(String organizadorId, Torneo torneo) {
        Organizador organizador = getOrThrow(organizadorId);
        if (!usuarioValidator.nombreValido(torneo.getNombre())) throw new IllegalArgumentException("nombre de torneo no valido");
        Torneo creado = torneoService.crear(torneo, Map.of());
        organizador.setCurrentTournament(creado);
        organizadorRepository.save(mapper.toEntity(organizador));
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
        return organizadorRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    public List<Torneo> getTorneos(String organizadorId) {
        getOrThrow(organizadorId);
        return torneoService.listar().stream()
                .filter(t -> organizadorRepository.findAll().stream()
                        .map(mapper::toDomain)
                        .anyMatch(o -> o.getId().equals(organizadorId)
                                && o.getCurrentTournament() != null
                                && o.getCurrentTournament().getId().equals(t.getId())))
                .toList();
    }

    public Torneo iniciarTorneoPorId(String organizadorId, String torneoId) {
        getOrThrow(organizadorId);
        return torneoService.iniciar(torneoId);
    }

    public Torneo finalizarTorneoPorId(String organizadorId, String torneoId) {
        getOrThrow(organizadorId);
        return torneoService.finalizar(torneoId);
    }

    private Organizador getOrThrow(String id) {
        return organizadorRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("organizador no encontrado"));
    }
}
