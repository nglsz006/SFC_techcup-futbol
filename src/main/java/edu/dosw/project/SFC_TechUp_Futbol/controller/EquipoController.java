package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.service.EquipoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Equipo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {
    private final EquipoService service;

    public EquipoController(EquipoService service) {
        this.service = service;
    }

    record EquipoRequest(String nombre, String escudo, String colorPrincipal, String colorSecundario, int capitanId) {}

    @PostMapping
    public Equipo crearEquipo(@RequestBody EquipoRequest req) {
        Equipo equipo = new Equipo();
        equipo.setNombre(req.nombre());
        equipo.setEscudo(req.escudo() != null ? req.escudo() : "");
        equipo.setColorPrincipal(req.colorPrincipal());
        equipo.setColorSecundario(req.colorSecundario() != null ? req.colorSecundario() : "");
        equipo.setCapitanId(req.capitanId());
        return service.crear(equipo, Map.of());
    }

    @GetMapping("/{id}")
    public Equipo obtenerEquipo(@PathVariable int id) {
        return service.obtener(id);
    }

    @GetMapping
    public List<Equipo> listarEquipos() {
        return service.listar();
    }

    @PostMapping("/{equipoId}/jugadores/{jugadorId}")
    public Equipo agregarJugador(@PathVariable int equipoId, @PathVariable int jugadorId) {
        return service.agregarJugador(equipoId, jugadorId);
    }
}
