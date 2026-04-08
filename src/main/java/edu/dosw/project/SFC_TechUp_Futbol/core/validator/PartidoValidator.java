package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Sancion;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PartidoValidator {

    public void validarCrearPartido(String torneoId, String equipoLocalId, String equipoVisitanteId,
                                    LocalDateTime fecha, String cancha) {
        if (torneoId == null || torneoId.isBlank()) throw new IllegalArgumentException("El id del torneo es obligatorio.");
        if (equipoLocalId == null || equipoLocalId.isBlank()) throw new IllegalArgumentException("El id del equipo local es obligatorio.");
        if (equipoVisitanteId == null || equipoVisitanteId.isBlank()) throw new IllegalArgumentException("El id del equipo visitante es obligatorio.");
        if (equipoLocalId.equals(equipoVisitanteId)) throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");
        if (fecha == null) throw new IllegalArgumentException("La fecha del partido es obligatoria.");
        if (cancha == null || cancha.isBlank()) throw new IllegalArgumentException("La cancha es obligatoria.");
    }

    public void validarResultado(int golesLocal, int golesVisitante) {
        if (golesLocal < 0 || golesVisitante < 0)
            throw new IllegalArgumentException("El marcador no puede ser negativo.");
    }

    public void validarGoleador(String jugadorId, int minuto) {
        if (jugadorId == null || jugadorId.isBlank()) throw new IllegalArgumentException("El id del jugador es obligatorio.");
        if (minuto < 1 || minuto > 120) throw new IllegalArgumentException("El minuto debe estar entre 1 y 120.");
    }

    public void validarTarjeta(String jugadorId, Partido.Tarjeta.TipoTarjeta tipo, int minuto) {
        if (jugadorId == null || jugadorId.isBlank()) throw new IllegalArgumentException("El id del jugador es obligatorio.");
        if (tipo == null) throw new IllegalArgumentException("El tipo de tarjeta es obligatorio.");
        if (minuto < 1 || minuto > 120) throw new IllegalArgumentException("El minuto debe estar entre 1 y 120.");
    }

    public void validarSancion(Sancion sancion) {
        if (sancion == null) throw new IllegalArgumentException("La sanción no puede ser nula.");
        if (sancion.getJugador() == null) throw new IllegalArgumentException("El jugador sancionado es obligatorio.");
        if (sancion.getTipoSancion() == null) throw new IllegalArgumentException("El tipo de sanción es obligatorio.");
        if (sancion.getDescripcion() == null || sancion.getDescripcion().isBlank()) throw new IllegalArgumentException("La descripción de la sanción es obligatoria.");
    }
}
