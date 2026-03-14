package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.JugadorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.UsuarioValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CapitanService {

    private final List<Capitan> capitanes = new ArrayList<>();
    private final JugadorService jugadorService;
    private final JugadorValidator jugadorValidator = new JugadorValidator();
    private final UsuarioValidator usuarioValidator = new UsuarioValidator();

    public CapitanService(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    public String crearEquipo(Long capitanId, String nombreEquipo) {
        Capitan capitan = buscarCapitanPorId(capitanId);
        if (capitan == null) throw new IllegalArgumentException("capitan no encontrado");
        if (!usuarioValidator.nombreValido(nombreEquipo)) throw new IllegalArgumentException("nombre de equipo no valido");
        return "equipo " + nombreEquipo + " creado por " + capitan.getName();
    }

    public String invitarJugador(Long capitanId, Long jugadorId) {
        Capitan capitan = buscarCapitanPorId(capitanId);
        if (capitan == null) throw new IllegalArgumentException("capitan no encontrado");
        Jugador jugador = jugadorService.buscarJugadorPorId(jugadorId);
        if (jugador == null) throw new IllegalArgumentException("jugador no encontrado");
        if (!jugadorValidator.jugadorDisponibleParaEquipo(jugador)) throw new IllegalArgumentException("jugador no disponible");
        return capitan.getName() + " invito a " + jugador.getName();
    }

    public String definirAlineacion(Long capitanId, List<Jugador> titulares) {
        Capitan capitan = buscarCapitanPorId(capitanId);
        if (capitan == null) throw new IllegalArgumentException("capitan no encontrado");
        if (titulares == null || titulares.size() < 7) throw new IllegalArgumentException("se necesitan minimo 7 titulares");
        return "alineacion lista con " + titulares.size() + " titulares";
    }

    public String subirComprobantePago(Long capitanId, String comprobante) {
        Capitan capitan = buscarCapitanPorId(capitanId);
        if (capitan == null) throw new IllegalArgumentException("capitan no encontrado");
        if (comprobante == null || comprobante.isBlank()) throw new IllegalArgumentException("el comprobante no puede estar vacio");
        return "comprobante subido por " + capitan.getName();
    }

    public List<Jugador> buscarJugadores(String posicion) {
        if (posicion == null || posicion.isBlank()) throw new IllegalArgumentException("debes indicar una posicion");
        List<Jugador> resultado = new ArrayList<>();
        for (Jugador jugador : jugadorService.getJugadores()) {
            if (jugador.getPosition().name().equalsIgnoreCase(posicion)) resultado.add(jugador);
        }
        return resultado;
    }

    public Capitan buscarCapitanPorId(Long id) {
        return capitanes.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Capitan> getCapitanes() {
        return capitanes;
    }
}
