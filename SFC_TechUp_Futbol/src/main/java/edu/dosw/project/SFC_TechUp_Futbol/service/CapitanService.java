package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.validators.JugadorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.validators.UsuarioValidator;
import java.util.ArrayList;
import java.util.List;


@Service
public class CapitanService {

    private List<Capitan> capitanes = new ArrayList<>();
    private List<Jugador> jugadoresDisponibles = new ArrayList<>();
    private JugadorValidator jugadorValidator = new JugadorValidator();
    private UsuarioValidator usuarioValidator = new UsuarioValidator();

    public void crearEquipo(Long capitanId, String nombreEquipo) {
        Capitan capitan = buscarCapitanPorId(capitanId);

        if (capitan == null) {
            System.out.println("Capitán no encontrado");
            return;
        }

        if (!usuarioValidator.nombreValido(nombreEquipo)) {
            System.out.println("El nombre del equipo no es válido");
            return;
        }

        System.out.println("Equipo " + nombreEquipo + " creado por " + capitan.getName());
    }

    public void invitarJugador(Long capitanId, Long jugadorId) {
        Capitan capitan = buscarCapitanPorId(capitanId);

        if (capitan == null) {
            System.out.println("Capitán no encontrado");
            return;
        }

        Jugador jugador = buscarJugadorDisponiblePorId(jugadorId);

        if (jugador == null) {
            System.out.println("Jugador no encontrado");
            return;
        }

        if (!jugadorValidator.jugadorDisponibleParaEquipo(jugador)) {
            System.out.println("El jugador no está disponible");
            return;
        }

        System.out.println("Capitán " + capitan.getName() + " invitó al jugador " + jugador.getName());
    }

    public void definirAlineacion(Long capitanId, List<Jugador> titulares) {
        Capitan capitan = buscarCapitanPorId(capitanId);

        if (capitan == null) {
            System.out.println("Capitán no encontrado");
            return;
        }

        if (titulares == null || titulares.size() < 7) {
            System.out.println("La alineación debe tener mínimo 7 titulares");
            return;
        }

        System.out.println("Alineación definida con " + titulares.size() + " titulares");
    }

    public void subirComprobantePago(Long capitanId, String comprobante) {
        Capitan capitan = buscarCapitanPorId(capitanId);

        if (capitan == null) {
            System.out.println("Capitán no encontrado");
            return;
        }

        if (comprobante == null || comprobante.trim().isEmpty()) {
            System.out.println("El comprobante no puede estar vacío");
            return;
        }

        System.out.println("Comprobante subido por " + capitan.getName() + ": " + comprobante);
    }

    public List<Jugador> buscarJugadores(List<Jugador> jugadores, String posicion) {
        List<Jugador> resultado = new ArrayList<>();

        if (posicion == null || posicion.trim().isEmpty()) {
            System.out.println("La posición no puede estar vacía");
            return resultado;
        }

        for (Jugador jugador : jugadores) {
            if (jugador.getPosition().name().equalsIgnoreCase(posicion)) {
                resultado.add(jugador);
            }
        }

        return resultado;
    }

    public Capitan buscarCapitanPorId(Long id) {
        for (Capitan capitan : capitanes) {
            if (capitan.getId().equals(id)) {
                return capitan;
            }
        }
        return null;
    }

    public Jugador buscarJugadorDisponiblePorId(Long id) {
        for (Jugador jugador : jugadoresDisponibles) {
            if (jugador.getId().equals(id)) {
                return jugador;
            }
        }
        return null;
    }

    public List<Jugador> getJugadoresDisponibles() {
        return jugadoresDisponibles;
    }

    public List<Capitan> getCapitanes() {
        return capitanes;
    }
}

