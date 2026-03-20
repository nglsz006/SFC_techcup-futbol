package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.CapitanRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.JugadorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.UsuarioValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CapitanService {

    private final CapitanRepository capitanRepository;
    private final JugadorService jugadorService;
    private final JugadorValidator jugadorValidator = new JugadorValidator();
    private final UsuarioValidator usuarioValidator = new UsuarioValidator();

    public CapitanService(CapitanRepository capitanRepository, JugadorService jugadorService) {
        this.capitanRepository = capitanRepository;
        this.jugadorService = jugadorService;
    }

    public Capitan save(Capitan capitan) {
        return capitanRepository.save(capitan);
    }

    public String crearEquipo(Long capitanId, String nombreEquipo) {
        Capitan capitan = getOrThrow(capitanId);
        if (!usuarioValidator.nombreValido(nombreEquipo)) throw new IllegalArgumentException("nombre de equipo no valido");
        return "equipo " + nombreEquipo + " creado por " + capitan.getName();
    }

    public String invitarJugador(Long capitanId, Long jugadorId) {
        Capitan capitan = getOrThrow(capitanId);
        Jugador jugador = jugadorService.buscarJugadorPorId(jugadorId);
        if (jugador == null) throw new IllegalArgumentException("jugador no encontrado");
        if (!jugadorValidator.jugadorDisponibleParaEquipo(jugador)) throw new IllegalArgumentException("jugador no disponible");
        return capitan.getName() + " invito a " + jugador.getName();
    }

    public String definirAlineacion(Long capitanId, List<Jugador> titulares) {
        getOrThrow(capitanId);
        if (titulares == null || titulares.size() < 7) throw new IllegalArgumentException("se necesitan minimo 7 titulares");
        return "alineacion lista con " + titulares.size() + " titulares";
    }

    public String subirComprobantePago(Long capitanId, String comprobante) {
        getOrThrow(capitanId);
        if (comprobante == null || comprobante.isBlank()) throw new IllegalArgumentException("el comprobante no puede estar vacio");
        return "comprobante subido por " + getOrThrow(capitanId).getName();
    }

    public List<Jugador> buscarJugadores(String posicion) {
        if (posicion == null || posicion.isBlank()) throw new IllegalArgumentException("debes indicar una posicion");
        return jugadorService.getJugadores().stream()
                .filter(j -> j.getPosition().name().equalsIgnoreCase(posicion))
                .toList();
    }

    public List<Capitan> getCapitanes() {
        return capitanRepository.findAll();
    }

    private Capitan getOrThrow(Long id) {
        return capitanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("capitan no encontrado"));
    }
}
