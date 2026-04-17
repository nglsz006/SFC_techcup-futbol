package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PartidoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.TorneoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Tournaments", description = "Creation, query, standings table, elimination bracket and tournament statistics. Accessible by Organizers.")
@RestController
@RequestMapping("/api/tournaments")
public class TorneoController {
    private final TorneoService service;
    private final PartidoService partidoService;
    private final EquipoService equipoService;

    public TorneoController(TorneoService service, PartidoService partidoService, EquipoService equipoService) {
        this.service = service;
        this.partidoService = partidoService;
        this.equipoService = equipoService;
    }

    record TorneoRequest(String nombre, String fechaInicio, String fechaFin, int cantidadEquipos, double costo) {}

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get tournament by ID", description = "Returns the information of a specific tournament. Accessible by all actors.")
    @GetMapping("/{id}")
    public Torneo obtenerTorneo(@PathVariable String id) {
        return service.obtener(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List tournaments", description = "Returns all tournaments registered in the system.")
    @GetMapping
    public List<Torneo> listarTorneos() {
        return service.listar();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Check if enrollment is open", description = "Returns whether the tournament accepts team enrollments.")
    @GetMapping("/{id}/enrollment")
    public Map<String, Object> inscripcionHabilitada(@PathVariable String id) {
        boolean habilitada = service.puedeInscribirEquipos(id);
        return Map.of("torneoId", id, "inscripcionHabilitada", habilitada);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Standings table", description = "Calculates and returns the tournament standings based on finished matches.")
    @GetMapping("/{id}/positions")
    public List<Map<String, Object>> tablaPosiciones(@PathVariable String id) {
        List<Partido> partidos = partidoService.consultarPartidosPorTorneo(id);
        Map<String, Map<String, Object>> tabla = new LinkedHashMap<>();

        for (Partido p : partidos) {
            if (p.getEstado() != Partido.PartidoEstado.FINALIZADO) continue;
            String localId = p.getEquipoLocal().getId();
            String visitanteId = p.getEquipoVisitante().getId();
            tabla.putIfAbsent(localId, crearFila(p.getEquipoLocal().getNombre()));
            tabla.putIfAbsent(visitanteId, crearFila(p.getEquipoVisitante().getNombre()));

            Map<String, Object> local = tabla.get(localId);
            Map<String, Object> visitante = tabla.get(visitanteId);
            int gl = p.getMarcadorLocal(), gv = p.getMarcadorVisitante();

            sumar(local, "gf", gl); sumar(local, "gc", gv); sumar(local, "pj", 1);
            sumar(visitante, "gf", gv); sumar(visitante, "gc", gl); sumar(visitante, "pj", 1);

            if (gl > gv) { sumar(local, "pts", 3); sumar(local, "pg", 1); sumar(visitante, "pp", 1); }
            else if (gl < gv) { sumar(visitante, "pts", 3); sumar(visitante, "pg", 1); sumar(local, "pp", 1); }
            else { sumar(local, "pts", 1); sumar(visitante, "pts", 1); sumar(local, "pe", 1); sumar(visitante, "pe", 1); }
        }
        return tabla.values().stream()
                .peek(f -> f.put("dg", (int) f.get("gf") - (int) f.get("gc")))
                .sorted((a, b) -> (int) b.get("pts") - (int) a.get("pts"))
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Elimination bracket", description = "Returns tournament matches grouped by phase.")
    @GetMapping("/{id}/bracket")
    public Map<String, List<Map<String, Object>>> llaveEliminatoria(@PathVariable String id) {
        List<Partido> partidos = partidoService.consultarPartidosPorTorneo(id);
        Map<String, List<Map<String, Object>>> bracket = new LinkedHashMap<>();
        for (Partido p : partidos) {
            if (p.getFase() == null) continue;
            String fase = p.getFase().name();
            bracket.putIfAbsent(fase, new ArrayList<>());
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("partidoId", p.getId());
            entry.put("local", p.getEquipoLocal() != null ? p.getEquipoLocal().getNombre() : "TBD");
            entry.put("localId", p.getEquipoLocal() != null ? p.getEquipoLocal().getId() : null);
            entry.put("visitante", p.getEquipoVisitante() != null ? p.getEquipoVisitante().getNombre() : "TBD");
            entry.put("visitanteId", p.getEquipoVisitante() != null ? p.getEquipoVisitante().getId() : null);
            entry.put("marcador", p.getMarcadorLocal() + " - " + p.getMarcadorVisitante());
            entry.put("estado", p.getEstado());
            entry.put("fecha", p.getFecha());
            bracket.get(fase).add(entry);
        }
        return bracket;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Tournament statistics", description = "Returns general metrics: total matches, goals, averages and match statuses.")
    @GetMapping("/{id}/statistics")
    public Map<String, Object> estadisticas(@PathVariable String id) {
        List<Partido> partidos = partidoService.consultarPartidosPorTorneo(id);
        int totalGoles = partidos.stream().mapToInt(p -> p.getMarcadorLocal() + p.getMarcadorVisitante()).sum();
        long finalizados = partidos.stream().filter(p -> p.getEstado() == Partido.PartidoEstado.FINALIZADO).count();
        long enCurso = partidos.stream().filter(p -> p.getEstado() == Partido.PartidoEstado.EN_CURSO).count();
        long programados = partidos.stream().filter(p -> p.getEstado() == Partido.PartidoEstado.PROGRAMADO).count();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalPartidos", partidos.size());
        stats.put("finalizados", finalizados);
        stats.put("enCurso", enCurso);
        stats.put("programados", programados);
        stats.put("totalGoles", totalGoles);
        stats.put("promedioGolesPorPartido", finalizados > 0 ? (double) totalGoles / finalizados : 0);
        return stats;
    }

    private Map<String, Object> crearFila(String nombre) {
        Map<String, Object> fila = new LinkedHashMap<>();
        fila.put("equipo", nombre);
        fila.put("pj", 0); fila.put("pg", 0); fila.put("pe", 0); fila.put("pp", 0);
        fila.put("gf", 0); fila.put("gc", 0); fila.put("pts", 0);
        return fila;
    }

    private void sumar(Map<String, Object> fila, String key, int valor) {
        fila.put(key, (int) fila.get(key) + valor);
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Operation(summary = "Generate bracket", description = "Automatically generates matches for the tournament bracket in random order.")
    @PostMapping("/{id}/generate-bracket")
    public List<Partido> generarLlaves(@PathVariable String id) {
        return partidoService.generarLlaves(id);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Top scorers", description = "Returns the top scorers of the tournament sorted by goals.")
    @GetMapping("/{id}/top-scorers")
    public List<Map<String, Object>> maximosGoleadores(@PathVariable String id) {
        return partidoService.maximosGoleadores(id);
    }

    @PreAuthorize("hasAnyRole('ORGANIZADOR', 'ADMINISTRADOR')")
    @Operation(
        summary = "Delete tournament",
        description = "Deletes a tournament by ID. Only ORGANIZADOR or ADMINISTRADOR can perform this action. Cannot delete a tournament that is currently IN PROGRESS."
    )
    @DeleteMapping("/{id}")
    public Map<String, String> eliminarTorneo(@PathVariable String id) {
        service.eliminar(id);
        return Map.of("mensaje", "Torneo eliminado correctamente.");
    }
}
