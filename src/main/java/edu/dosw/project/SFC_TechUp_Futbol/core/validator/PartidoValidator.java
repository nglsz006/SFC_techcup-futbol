package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PartidoValidator {

    public void validarCrearPartido(Long torneoId, Long equipoLocalId, Long equipoVisitanteId,
                                    LocalDateTime fecha, String cancha) {
        if (torneoId == null) throw new IllegalArgumentException("El id del torneo es obligatorio.");
        if (equipoLocalId == null) throw new IllegalArgumentException("El id del equipo local es obligatorio.");
        if (equipoVisitanteId == null) throw new IllegalArgumentException("El id del equipo visitante es obligatorio.");
        if (equipoLocalId.equals(equipoVisitanteId)) throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo.");
        if (fecha == null) throw new IllegalArgumentException("La fecha del partido es obligatoria.");
        if (cancha == null || cancha.isBlank()) throw new IllegalArgumentException("La cancha es obligatoria.");
    }

    public void validarResultado(int golesLocal, int golesVisitante) {
        if (golesLocal < 0 || golesVisitante < 0) {
            throw new IllegalArgumentException("El marcador no puede ser negativo.");
        }
    }

    public void validarGoleador(Long jugadorId, int minuto) {
        if (jugadorId == null) throw new IllegalArgumentException("El id del jugador es obligatorio.");
        if (minuto < 1 || minuto > 120) throw new IllegalArgumentException("El minuto debe estar entre 1 y 120.");
    }

    public void validarTarjeta(Long jugadorId, Partido.Tarjeta.TipoTarjeta tipo, int minuto) {
        if (jugadorId == null) throw new IllegalArgumentException("El id del jugador es obligatorio.");
        if (tipo == null) throw new IllegalArgumentException("El tipo de tarjeta es obligatorio (AMARILLA o ROJA).");
        if (minuto < 1 || minuto > 120) throw new IllegalArgumentException("El minuto debe estar entre 1 y 120.");
    }
}
