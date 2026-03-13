package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.model.Tarjeta;
import edu.dosw.project.SFC_TechUp_Futbol.service.PartidoService;
import edu.dosw.project.SFC_TechUp_Futbol.validators.PartidoValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PartidoController {

    private final PartidoService partidoService;
    private final PartidoValidator partidoValidator;

    public PartidoController(PartidoService partidoService, PartidoValidator partidoValidator) {
        this.partidoService = partidoService;
        this.partidoValidator = partidoValidator;
    }

    public Partido crearPartido(Map<String, Object> body) {
        Long torneoId = Long.valueOf(body.get("torneoId").toString());
        Long equipoLocalId = Long.valueOf(body.get("equipoLocalId").toString());
        Long equipoVisitanteId = Long.valueOf(body.get("equipoVisitanteId").toString());
        LocalDateTime fecha = LocalDateTime.parse(body.get("fecha").toString());
        String cancha = body.get("cancha").toString();
        partidoValidator.validarCrearPartido(torneoId, equipoLocalId, equipoVisitanteId, fecha, cancha);
        return partidoService.crearPartido(torneoId, equipoLocalId, equipoVisitanteId, fecha, cancha);
    }

    public Partido iniciarPartido(Long id) {
        return partidoService.iniciarPartido(id);
    }

    public Partido registrarResultado(Long id, Map<String, Integer> body) {
        int golesLocal = body.get("golesLocal");
        int golesVisitante = body.get("golesVisitante");
        partidoValidator.validarResultado(golesLocal, golesVisitante);
        return partidoService.registrarResultado(id, golesLocal, golesVisitante);
    }

    public Partido finalizarPartido(Long id) {
        return partidoService.finalizarPartido(id);
    }

    public Partido registrarGoleador(Long id, Map<String, Object> body) {
        Long jugadorId = Long.valueOf(body.get("jugadorId").toString());
        int minuto = Integer.parseInt(body.get("minuto").toString());
        partidoValidator.validarGoleador(jugadorId, minuto);
        return partidoService.registrarGoleador(id, jugadorId, minuto);
    }

    public Partido registrarTarjeta(Long id, Map<String, Object> body) {
        Long jugadorId = Long.valueOf(body.get("jugadorId").toString());
        Tarjeta.TipoTarjeta tipo = Tarjeta.TipoTarjeta.valueOf(body.get("tipo").toString());
        int minuto = Integer.parseInt(body.get("minuto").toString());
        partidoValidator.validarTarjeta(jugadorId, tipo, minuto);
        return partidoService.registrarTarjeta(id, jugadorId, tipo, minuto);
    }

    public Partido consultarPartido(Long id) {
        return partidoService.consultarPartido(id);
    }

    public List<Partido> consultarPorTorneo(Long torneoId) {
        return partidoService.consultarPartidosPorTorneo(torneoId);
    }

    public List<Partido> consultarPorEquipo(Long equipoId) {
        return partidoService.consultarPartidosPorEquipo(equipoId);
    }
}
