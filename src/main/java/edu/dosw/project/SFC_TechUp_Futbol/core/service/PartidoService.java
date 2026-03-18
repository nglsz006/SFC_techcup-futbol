package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;

import java.time.LocalDateTime;
import java.util.List;

public interface PartidoService {
    Partido crearPartido(Long torneoId, Long equipoLocalId, Long equipoVisitanteId, LocalDateTime fecha, String cancha);
    Partido iniciarPartido(Long partidoId);
    Partido registrarResultado(Long partidoId, int golesLocal, int golesVisitante);
    Partido finalizarPartido(Long partidoId);
    Partido registrarGoleador(Long partidoId, Long jugadorId, int minuto);
    Partido registrarTarjeta(Long partidoId, Long jugadorId, Partido.Tarjeta.TipoTarjeta tipo, int minuto);
    Partido consultarPartido(Long partidoId);
    List<Partido> consultarPartidosPorTorneo(Long torneoId);
    List<Partido> consultarPartidosPorEquipo(Long equipoId);
}