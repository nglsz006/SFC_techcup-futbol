package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PartidoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.TorneoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Tournaments", description = "Creation, query, standings table, elimination bracket and tournament statistics. Accessible by Organizers.")
@RestController
@RequestMapping("/api/torneos")
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

    @Operation(summary = "Get tournament by ID", description = "Returns the information of a specific tournament. Accessible by all actors.")
    @GetMapping("/{id}")
    public Torneo obtenerTorneo(@PathVariable int id) {
        return service.obtener(id);
    }

    @Operation(summary = "List tournaments", description = "Returns all tournaments registered in the system.")
    @GetMapping
    public List<Torneo> listarTorneos() {
        return service.listar();
    }

    @Operation(summary = "Standings table", description = "Calculates and returns the tournament standings based on finished matches.")
    @GetMapping("/{id}/posiciones")
    public List<Map<String, Object>> tablaPosiciones(@PathVariable int id) {
        List<Partido> partidos = partidoService.consultarPartidosPorTorneo((long) id);
        Map<Integer, Map<String, Object>> tabla = new LinkedHashMap<>();

        for (Partido p : partidos) {
            if (p.getEstado() != Partido.PartidoEstado.FINALIZADO) continue;
            int localId = p.getEquipoLocal().getId();
            int visitanteId = p.getEquipoVisitante().getId();
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
                .sorted((a, b) -> (int) b.get("pts") - (int) a.get("pts"))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Elimination bracket", description = "Returns all tournament matches with their status, score and date to display the bracket.")
    @GetMapping("/{id}/llave")
    public List<Map<String, Object>> llaveEliminatoria(@PathVariable int id) {
        List<Partido> partidos = partidoService.consultarPartidosPorTorneo((long) id);
        return partidos.stream().map(p -> {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("partidoId", p.getId());
            entry.put("local", p.getEquipoLocal() != null ? p.getEquipoLocal().getNombre() : "TBD");
            entry.put("visitante", p.getEquipoVisitante() != null ? p.getEquipoVisitante().getNombre() : "TBD");
            entry.put("marcador", p.getMarcadorLocal() + " - " + p.getMarcadorVisitante());
            entry.put("estado", p.getEstado());
            entry.put("fecha", p.getFecha());
            return entry;
        }).collect(Collectors.toList());
    }

    @Operation(summary = "Tournament statistics", description = "Returns general metrics: total matches, goals, averages and match statuses.")
    @GetMapping("/{id}/estadisticas")
    public Map<String, Object> estadisticas(@PathVariable int id) {
        List<Partido> partidos = partidoService.consultarPartidosPorTorneo((long) id);
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
}
