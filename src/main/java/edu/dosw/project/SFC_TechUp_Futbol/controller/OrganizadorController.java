package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.OrganizadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Organizadores", description = "Gestion de organizadores")
@RestController
@RequestMapping("/organizadores")
public class OrganizadorController {

    private final OrganizadorService organizadorService;

    public OrganizadorController(OrganizadorService organizadorService) {
        this.organizadorService = organizadorService;
    }

    @Operation(summary = "Crear organizador")
    @PostMapping
    public Organizador crearOrganizador(@RequestBody Map<String, Object> body) {
        Organizador organizador = new Organizador(
            null,
            body.get("nombre").toString(),
            body.get("email").toString(),
            body.get("password").toString(),
            Usuario.TipoUsuario.valueOf(body.get("tipoUsuario").toString()),
            null
        );
        return organizadorService.save(organizador);
    }

    @Operation(summary = "Crear torneo")
    @PostMapping("/{id}/torneo")
    public Torneo crearTorneo(@PathVariable Long id, @RequestBody Torneo torneo) {
        return organizadorService.crearTorneo(id, torneo);
    }

    @Operation(summary = "Iniciar torneo")
    @PatchMapping("/{id}/torneo/iniciar")
    public Torneo iniciarTorneo(@PathVariable Long id) {
        return organizadorService.iniciarTorneo(id);
    }

    @Operation(summary = "Finalizar torneo")
    @PatchMapping("/{id}/torneo/finalizar")
    public Torneo finalizarTorneo(@PathVariable Long id) {
        return organizadorService.finalizarTorneo(id);
    }
}
