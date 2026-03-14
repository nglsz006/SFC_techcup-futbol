package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Tarjeta;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.PartidoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PartidoValidator;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    private final PartidoService partidoService;
    private final PartidoValidator partidoValidator;

    public PartidoController(PartidoService partidoService, PartidoValidator partidoValidator) {
        this.partidoService = partidoService;
        this.partidoValidator = partidoValidator;
    }

    record PartidoRequest(Long torneoId, Long equipoLocalId, Long equipoVisitanteId, String fecha, String cancha) {}
    record ResultadoRequest(int golesLocal, int golesVisitante) {}
    record GolRequest(Long jugadorId, int minuto) {}
    record TarjetaRequest(Long jugadorId, String tipo, int minuto) {}

    @PostMapping
    public Partido crearPartido(@RequestBody PartidoRequest req) {
        LocalDateTime fecha = LocalDateTime.parse(req.fecha());
        partidoValidator.validarCrearPartido(req.torneoId(), req.equipoLocalId(), req.equipoVisitanteId(), fecha, req.cancha());
        return partidoService.crearPartido(req.torneoId(), req.equipoLocalId(), req.equipoVisitanteId(), fecha, req.cancha());
    }

    @PutMapping("/{id}/iniciar")
    public Partido iniciarPartido(@PathVariable Long id) {
        return partidoService.iniciarPartido(id);
    }

    @PutMapping("/{id}/resultado")
    public Partido registrarResultado(@PathVariable Long id, @RequestBody ResultadoRequest req) {
        partidoValidator.validarResultado(req.golesLocal(), req.golesVisitante());
        return partidoService.registrarResultado(id, req.golesLocal(), req.golesVisitante());
    }

    @PutMapping("/{id}/finalizar")
    public Partido finalizarPartido(@PathVariable Long id) {
        return partidoService.finalizarPartido(id);
    }

    @PostMapping("/{id}/goles")
    public Partido registrarGoleador(@PathVariable Long id, @RequestBody GolRequest req) {
        partidoValidator.validarGoleador(req.jugadorId(), req.minuto());
        return partidoService.registrarGoleador(id, req.jugadorId(), req.minuto());
    }

    @PostMapping("/{id}/tarjetas")
    public Partido registrarTarjeta(@PathVariable Long id, @RequestBody TarjetaRequest req) {
        Tarjeta.TipoTarjeta tipo = Tarjeta.TipoTarjeta.valueOf(req.tipo());
        partidoValidator.validarTarjeta(req.jugadorId(), tipo, req.minuto());
        return partidoService.registrarTarjeta(id, req.jugadorId(), tipo, req.minuto());
    }

    @GetMapping("/{id}")
    public Partido consultarPartido(@PathVariable Long id) {
        return partidoService.consultarPartido(id);
    }

    @GetMapping("/torneo/{torneoId}")
    public List<Partido> consultarPorTorneo(@PathVariable Long torneoId) {
        return partidoService.consultarPartidosPorTorneo(torneoId);
    }

    @GetMapping("/equipo/{equipoId}")
    public List<Partido> consultarPorEquipo(@PathVariable Long equipoId) {
        return partidoService.consultarPartidosPorEquipo(equipoId);
    }
}
