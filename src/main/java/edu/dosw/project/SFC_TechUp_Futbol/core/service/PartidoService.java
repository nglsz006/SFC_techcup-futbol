package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PartidoService {
    Partido crearPartido(String torneoId, String equipoLocalId, String equipoVisitanteId, LocalDateTime fecha, String cancha);
    Partido iniciarPartido(String partidoId);
    Partido registrarResultado(String partidoId, int golesLocal, int golesVisitante);
    Partido finalizarPartido(String partidoId);
    Partido registrarGoleador(String partidoId, String jugadorId, int minuto);
    Partido registrarTarjeta(String partidoId, String jugadorId, Partido.Tarjeta.TipoTarjeta tipo, int minuto);
    Partido registrarSancion(String partidoId, String jugadorId, Sancion.TipoSancion tipoSancion, String descripcion);
    Partido consultarPartido(String partidoId);
    Map<String, Object> consultarEventos(String partidoId);
    List<Partido> consultarPartidosPorTorneo(String torneoId);
    List<Partido> consultarPartidosPorEquipo(String equipoId);
    List<Partido> consultarPartidosPorEstado(Partido.PartidoEstado estado);
    List<Partido> generarLlaves(String torneoId);
    List<Map<String, Object>> maximosGoleadores(String torneoId);
}
