package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.JugadorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class JugadorService {

    private final JugadorJpaRepository jugadorRepository;
    private final JugadorMapper mapper;
    private final JugadorValidator jugadorValidator = new JugadorValidator();

    public JugadorService(JugadorJpaRepository jugadorRepository, JugadorMapper mapper) {
        this.jugadorRepository = jugadorRepository;
        this.mapper = mapper;
    }

    public Jugador save(Jugador jugador) {
        if (jugador.getId() == null) jugador.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jugadorRepository.save(mapper.toEntity(jugador)));
    }

    public Jugador editarPerfil(String jugadorId, String nombre, int numeroCamiseta, Jugador.Posicion posicion, String foto) {
        Jugador jugador = getOrThrow(jugadorId);
        if (nombre != null && !nombre.isBlank()) jugador.setName(nombre);
        if (numeroCamiseta > 0) jugador.setJerseyNumber(numeroCamiseta);
        if (posicion != null) jugador.setPosition(posicion);
        if (foto != null && !foto.isBlank()) jugador.setPhoto(foto);
        if (jugador.getId() == null) jugador.setId(IdGeneratorUtil.generarId());
        return mapper.toDomain(jugadorRepository.save(mapper.toEntity(jugador)));
    }

    public void aceptarInvitacion(String jugadorId) {
        Jugador jugador = getOrThrow(jugadorId);
        jugador.setAvailable(false);
        jugadorRepository.save(mapper.toEntity(jugador));
    }

    public void rechazarInvitacion(String jugadorId) {
        Jugador jugador = getOrThrow(jugadorId);
        jugador.setAvailable(true);
        jugadorRepository.save(mapper.toEntity(jugador));
    }

    public void marcarDisponible(String jugadorId) {
        Jugador jugador = getOrThrow(jugadorId);
        if (!jugadorValidator.jugadorDisponibleParaEquipo(jugador))
            throw new IllegalStateException("ese jugador ya pertenece a un equipo.");
        jugador.setAvailable(true);
        jugadorRepository.save(mapper.toEntity(jugador));
    }

    public Jugador buscarJugadorPorId(String id) {
        return jugadorRepository.findById(id).map(mapper::toDomain).orElse(null);
    }

    public String buscarIdPorEmail(String email) {
        return jugadorRepository.findByEmail(Base64Util.encode(email))
                .map(e -> e.getId())
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado con ese correo."));
    }

    public void eliminar(String id) {
        Jugador jugador = getOrThrow(id);
        if (jugador.getEquipo() != null && !jugador.getEquipo().isBlank())
            throw new IllegalStateException("No se puede eliminar un jugador que pertenece a un equipo.");
        jugadorRepository.deleteById(id);
    }

    public List<Jugador> getJugadores() {
        return jugadorRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    private Jugador getOrThrow(String id) {
        return jugadorRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado."));
    }

    public String subirFoto(String jugadorId, MultipartFile file) {
        Jugador jugador = getOrThrow(jugadorId);
        if (file.isEmpty()) throw new IllegalArgumentException("El archivo está vacío");
        try {
            Path base = Paths.get("uploads").toAbsolutePath().normalize();
            File directorio = base.toFile();
            if (!directorio.exists() && !directorio.mkdirs())
                throw new RuntimeException("No se pudo crear el directorio de uploads");
            String nombreOriginal = file.getOriginalFilename();
            if (nombreOriginal == null || nombreOriginal.isBlank())
                throw new IllegalArgumentException("Nombre de archivo inválido");
            String nombreSeguro = IdGeneratorUtil.generarId() + "_"
                    + Paths.get(nombreOriginal).getFileName().toString();
            Path destino = base.resolve(nombreSeguro).normalize();
            if (!destino.startsWith(base))
                throw new IllegalArgumentException("Ruta de archivo no permitida");
            file.transferTo(destino.toFile());
            jugador.setPhoto(destino.toString());
            jugadorRepository.save(mapper.toEntity(jugador));
            return "Imagen subida correctamente";
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen");
        }
    }
}
