package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.service.TorneoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {
    private final TorneoService service;

    public TorneoController(TorneoService service) {
        this.service = service;
    }

    record TorneoRequest(String nombre, String fechaInicio, String fechaFin, int cantidadEquipos, double costo) {}

    @PostMapping
    public Torneo crearTorneo(@RequestBody TorneoRequest req) {
        Torneo torneo = new Torneo();
        torneo.setNombre(req.nombre());
        torneo.setFechaInicio(LocalDateTime.parse(req.fechaInicio()));
        torneo.setFechaFin(LocalDateTime.parse(req.fechaFin()));
        torneo.setCantidadEquipos(req.cantidadEquipos());
        torneo.setCosto(req.costo());
        return service.crear(torneo, Map.of());
    }

    @GetMapping("/{id}")
    public Torneo obtenerTorneo(@PathVariable int id) {
        return service.obtener(id);
    }

    @GetMapping
    public List<Torneo> listarTorneos() {
        return service.listar();
    }

    @PutMapping("/{id}/iniciar")
    public Torneo iniciarTorneo(@PathVariable int id) {
        return service.iniciar(id);
    }

    @PutMapping("/{id}/finalizar")
    public Torneo finalizarTorneo(@PathVariable int id) {
        return service.finalizar(id);
    }
}
