package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.OrganizadorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizadores")
public class OrganizadorController {

    private final OrganizadorService organizadorService;

    public OrganizadorController(OrganizadorService organizadorService) {
        this.organizadorService = organizadorService;
    }

    @PostMapping("/{id}/torneo")
    public Torneo crearTorneo(@PathVariable Long id, @RequestBody Torneo torneo) {
        return organizadorService.crearTorneo(id, torneo);
    }

    @PatchMapping("/{id}/torneo/iniciar")
    public Torneo iniciarTorneo(@PathVariable Long id) {
        return organizadorService.iniciarTorneo(id);
    }

    @PatchMapping("/{id}/torneo/finalizar")
    public Torneo finalizarTorneo(@PathVariable Long id) {
        return organizadorService.finalizarTorneo(id);
    }
}
