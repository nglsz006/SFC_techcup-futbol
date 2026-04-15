package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Capitan;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.JugadorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.UsuarioValidator;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.CapitanEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.CapitanMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.CapitanJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CapitanService {

    private final CapitanJpaRepository capitanRepository;
    private final CapitanMapper mapper;
    private final JugadorService jugadorService;
    private final JugadorJpaRepository jugadorRepository;
    private final JugadorMapper jugadorMapper;
    private final JugadorValidator jugadorValidator = new JugadorValidator();
    private final UsuarioValidator usuarioValidator = new UsuarioValidator();

    public CapitanService(CapitanJpaRepository capitanRepository, CapitanMapper mapper, JugadorService jugadorService,
                          JugadorJpaRepository jugadorRepository, JugadorMapper jugadorMapper) {
        this.capitanRepository = capitanRepository;
        this.mapper = mapper;
        this.jugadorService = jugadorService;
        this.jugadorRepository = jugadorRepository;
        this.jugadorMapper = jugadorMapper;
    }

    public Capitan save(Capitan capitan) {
        if (capitan.getId() == null) capitan.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(capitanRepository.save(mapper.toEntity(capitan)));
    }

    public String crearEquipo(String capitanId, String nombreEquipo) {
        Capitan capitan = getOrThrow(capitanId);
        if (!usuarioValidator.nombreValido(nombreEquipo)) throw new IllegalArgumentException("nombre de equipo no valido");
        return "equipo " + nombreEquipo + " creado por " + capitan.getName();
    }

    public String invitarJugador(String capitanId, String jugadorId) {
        Capitan capitan = getOrThrow(capitanId);
        Jugador jugador = jugadorService.buscarJugadorPorId(jugadorId);
        if (jugador == null) throw new IllegalArgumentException("jugador no encontrado");
        if (!jugadorValidator.jugadorDisponibleParaEquipo(jugador)) throw new IllegalArgumentException("jugador no disponible");
        return capitan.getName() + " invito a " + jugador.getName();
    }

    public String definirAlineacion(String capitanId, List<Jugador> titulares) {
        getOrThrow(capitanId);
        if (titulares == null || titulares.size() < 7) throw new IllegalArgumentException("se necesitan minimo 7 titulares");
        return "alineacion lista con " + titulares.size() + " titulares";
    }

    public String subirComprobantePago(String capitanId, String comprobante) {
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
        return capitanRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    public String toggleRol(String usuarioId) {
        // Si es capitán, convertir a jugador
        if (capitanRepository.existsById(usuarioId)) {
            CapitanEntity capitan = capitanRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Capitan no encontrado"));
            capitanRepository.deleteById(usuarioId);
            JugadorEntity jugador = new JugadorEntity();
            jugador.setId(capitan.getId());
            jugador.setName(capitan.getName());
            jugador.setEmail(capitan.getEmail());
            jugador.setPassword(capitan.getPassword());
            jugador.setUserType(capitan.getUserType());
            jugador.setJerseyNumber(capitan.getJerseyNumber());
            jugador.setPosition(capitan.getPosition());
            jugador.setAvailable(capitan.isAvailable());
            jugador.setPhoto(capitan.getPhoto());
            jugador.setEquipoId(capitan.getEquipoId());
            jugadorRepository.save(jugador);
            return "Rol cambiado a JUGADOR";
        }
        // Si es jugador, convertir a capitán
        if (jugadorRepository.existsById(usuarioId)) {
            JugadorEntity jugador = jugadorRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado"));
            jugadorRepository.deleteById(usuarioId);
            CapitanEntity capitan = new CapitanEntity();
            capitan.setId(jugador.getId());
            capitan.setName(jugador.getName());
            capitan.setEmail(jugador.getEmail());
            capitan.setPassword(jugador.getPassword());
            capitan.setUserType(jugador.getUserType());
            capitan.setJerseyNumber(jugador.getJerseyNumber());
            capitan.setPosition(jugador.getPosition());
            capitan.setAvailable(jugador.isAvailable());
            capitan.setPhoto(jugador.getPhoto());
            capitan.setEquipoId(jugador.getEquipoId());
            capitanRepository.save(capitan);
            return "Rol cambiado a CAPITAN";
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    private Capitan getOrThrow(String id) {
        return capitanRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("capitan no encontrado"));
    }
}
