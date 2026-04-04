package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AlineacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Lineups", description = "Lineup management: register and query lineups.")
@RestController
@RequestMapping("/api/lineups")
public class AlineacionController {

    private final AlineacionService service;

    public AlineacionController(AlineacionService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('CAPITAN')")
    @Operation(summary = "Register lineup", description = "Allows a captain to register a lineup for a match.")
    @PostMapping
    public Alineacion registrarAlineacion(@RequestBody Map<String, Object> body) {
        String equipoId = body.get("equipoId").toString();
        String partidoId = body.get("partidoId").toString();
        String formacionStr = body.get("formacion").toString();
        Alineacion.Formacion formacion = Alineacion.Formacion.fromString(formacionStr);
        if (formacion == null)
            throw new IllegalArgumentException("Formacion invalida. Use: 4-4-2, 4-3-3, 3-5-2, 4-5-1 o 5-3-2");
        List<String> titulares = ((List<?>) body.get("titulares")).stream().map(Object::toString).toList();
        List<String> reservas = body.containsKey("reservas")
                ? ((List<?>) body.get("reservas")).stream().map(Object::toString).toList()
                : List.of();
        return service.registrar(equipoId, partidoId, formacion, titulares, reservas);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get lineup by ID", description = "Returns the lineup registered for a specific match.")
    @GetMapping("/{id}")
    public Alineacion obtenerAlineacion(@PathVariable String id) {
        return service.obtener(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List all lineups", description = "Returns all lineups registered in the system.")
    @GetMapping
    public List<Alineacion> listarAlineaciones() {
        return service.listar();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get rival lineup", description = "Returns the rival team lineup for a given match.")
    @GetMapping("/rival")
    public Alineacion obtenerAlineacionRival(@RequestParam String partidoId, @RequestParam String equipoId) {
        return service.obtenerRival(partidoId, equipoId);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get lineup by match and team", description = "Returns the lineup for a specific team in a match.")
    @GetMapping("/match/{partidoId}/team/{equipoId}")
    public Alineacion obtenerPorPartidoYEquipo(@PathVariable String partidoId, @PathVariable String equipoId) {
        return service.obtenerPorPartidoYEquipo(partidoId, equipoId);
    }
}
