package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.JugadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.JugadorValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final JugadorValidator jugadorValidator = new JugadorValidator();

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public Jugador editarPerfil(String jugadorId, String nombre, int numeroCamiseta, Jugador.Posicion posicion, String foto) {
        Jugador jugador = getOrThrow(jugadorId);
        if (nombre != null && !nombre.isBlank()) jugador.setName(nombre);
        if (numeroCamiseta > 0) jugador.setJerseyNumber(numeroCamiseta);
        if (posicion != null) jugador.setPosition(posicion);
        if (foto != null && !foto.isBlank()) jugador.setPhoto(foto);
        return jugadorRepository.save(jugador);
    }

    public void aceptarInvitacion(String jugadorId) {
        Jugador jugador = getOrThrow(jugadorId);
        jugador.setAvailable(false);
        jugadorRepository.save(jugador);
    }

    public void rechazarInvitacion(String jugadorId) {
        Jugador jugador = getOrThrow(jugadorId);
        jugador.setAvailable(true);
        jugadorRepository.save(jugador);
    }

    public void marcarDisponible(String jugadorId) {
        Jugador jugador = getOrThrow(jugadorId);
        if (!jugadorValidator.jugadorDisponibleParaEquipo(jugador))
            throw new IllegalStateException("ese jugador ya pertenece a un equipo.");
        jugador.setAvailable(true);
        jugadorRepository.save(jugador);
    }

    public Jugador buscarJugadorPorId(String id) {
        return jugadorRepository.findById(id).orElse(null);
    }

    public List<Jugador> getJugadores() {
        return jugadorRepository.findAll();
    }

    private Jugador getOrThrow(String id) {
        return jugadorRepository.findById(id)
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
            jugadorRepository.save(jugador);
            return "Imagen subida correctamente";
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen");
        }
    }
}
