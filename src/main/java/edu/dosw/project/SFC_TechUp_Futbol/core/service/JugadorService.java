package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.JugadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.JugadorValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final JugadorValidator jugadorValidator = new JugadorValidator();

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public Jugador editarPerfil(Long jugadorId, String nombre, int numeroCamiseta, Jugador.Posicion posicion, String foto) {
        Jugador jugador = getOrThrow(jugadorId);
        if (nombre != null && !nombre.isBlank()) jugador.setName(nombre);
        if (numeroCamiseta > 0) jugador.setJerseyNumber(numeroCamiseta);
        if (posicion != null) jugador.setPosition(posicion);
        if (foto != null && !foto.isBlank()) jugador.setPhoto(foto);
        return jugadorRepository.save(jugador);
    }

    public void aceptarInvitacion(Long jugadorId) {
        Jugador jugador = getOrThrow(jugadorId);
        jugador.setAvailable(false);
        jugadorRepository.save(jugador);
    }

    public void rechazarInvitacion(Long jugadorId) {
        Jugador jugador = getOrThrow(jugadorId);
        jugador.setAvailable(true);
        jugadorRepository.save(jugador);
    }

    public void marcarDisponible(Long jugadorId) {
        Jugador jugador = getOrThrow(jugadorId);
        if (!jugadorValidator.jugadorDisponibleParaEquipo(jugador))
            throw new IllegalStateException("ese jugador ya pertenece a un equipo.");
        jugador.setAvailable(true);
        jugadorRepository.save(jugador);
    }

    public Jugador buscarJugadorPorId(Long id) {
        return jugadorRepository.findById(id).orElse(null);
    }

    public List<Jugador> getJugadores() {
        return jugadorRepository.findAll();
    }

    private Jugador getOrThrow(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado con id: " + id));
    }

    public String subirFoto(Long jugadorId, MultipartFile file) {
        Jugador jugador = getOrThrow(jugadorId);

        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        try {
            // carpeta donde guardar
            String carpeta = "uploads/";
            File directorio = new File(carpeta);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // generar nombre único
            String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // ruta completa
            String ruta = carpeta + nombreArchivo;

            // guardar archivo
            file.transferTo(new File(ruta));

            // guardar ruta en el jugador
            jugador.setPhoto(ruta);
            jugadorRepository.save(jugador);

            return "Imagen subida correctamente: " + ruta;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen");
        }
    }
}
