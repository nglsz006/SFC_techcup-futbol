package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.JugadorValidator;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class JugadorService {

    private List<Jugador> jugadores = new ArrayList<>();
    private JugadorValidator jugadorValidator = new JugadorValidator();

    public void aceptarInvitacion(Long jugadorId) {
        Jugador jugador = buscarJugadorPorId(jugadorId);

        if (jugador == null) {
            System.out.println("no encontre ese jugador, revisa el ID.");
            return;
        }

        jugador.setAvailable(false);
    }

    public void rechazarInvitacion(Long jugadorId) {
        Jugador jugador = buscarJugadorPorId(jugadorId);

        if (jugador == null) {
            System.out.println("no encontre ese jugador, revisa el ID.");
            return;
        }

        jugador.setAvailable(true);
    }

    public void marcarDisponible(Long jugadorId) {
        Jugador jugador = buscarJugadorPorId(jugadorId);

        if (jugador == null) {
            System.out.println("no encontre ese jugador, revisa el ID.");
            return;
        }

        if (!jugadorValidator.jugadorDisponibleParaEquipo(jugador)) {
            System.out.println("ese jugador ya pertenece a un equipo.");
            return;
        }

        jugador.setAvailable(true);
    }

    public Jugador buscarJugadorPorId(Long id) {
        for (Jugador jugador : jugadores) {
            if (jugador.getId().equals(id)) {
                return jugador;
            }
        }
        return null;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }
}


