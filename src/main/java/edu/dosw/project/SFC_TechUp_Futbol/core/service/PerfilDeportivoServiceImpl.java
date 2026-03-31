package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.JugadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.PerfilDeportivoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PerfilDeportivoValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PerfilDeportivoServiceImpl implements PerfilDeportivoService {

    private static final Logger log = Logger.getLogger(PerfilDeportivoServiceImpl.class.getName());

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    private final PerfilDeportivoRepository perfilRepository;
    private final JugadorRepository jugadorRepository;
    private final PerfilDeportivoValidator validator;

    public PerfilDeportivoServiceImpl(PerfilDeportivoRepository perfilRepository,
                                      JugadorRepository jugadorRepository) {
        this.perfilRepository = perfilRepository;
        this.jugadorRepository = jugadorRepository;
        this.validator = new PerfilDeportivoValidator();
    }

    @Override
    public PerfilDeportivo crearPerfil(String jugadorId, List<Jugador.Posicion> posiciones, int dorsal,
                                       String foto, int edad, PerfilDeportivo.Genero genero,
                                       String identificacion, Integer semestre) {
        jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado."));

        if (perfilRepository.findByJugadorId(jugadorId).isPresent()) {
            throw new IllegalStateException("El jugador ya tiene un perfil deportivo creado.");
        }

        validator.validarPerfil(posiciones, dorsal, edad, genero, identificacion);
        validator.validarSemestre(semestre);

        PerfilDeportivo perfil = new PerfilDeportivo(null, jugadorId, posiciones, dorsal,
                foto, edad, genero, identificacion, semestre);

        PerfilDeportivo guardado = perfilRepository.save(perfil);
        log.info("Perfil deportivo creado para jugador: " + sanitize(jugadorId));
        return guardado;
    }

    @Override
    public PerfilDeportivo editarPerfil(String jugadorId, List<Jugador.Posicion> posiciones, int dorsal,
                                        String foto, int edad, PerfilDeportivo.Genero genero,
                                        String identificacion, Integer semestre) {
        PerfilDeportivo perfil = perfilRepository.findByJugadorId(jugadorId)
                .orElseThrow(() -> new IllegalArgumentException("El jugador no tiene perfil deportivo. Crea uno primero."));

        validator.validarPerfil(posiciones, dorsal, edad, genero, identificacion);
        validator.validarSemestre(semestre);

        perfil.setPosiciones(posiciones);
        perfil.setDorsal(dorsal);
        if (foto != null && !foto.isBlank()) perfil.setFoto(foto);
        perfil.setEdad(edad);
        perfil.setGenero(genero);
        perfil.setIdentificacion(identificacion);
        perfil.setSemestre(semestre);

        PerfilDeportivo actualizado = perfilRepository.save(perfil);
        log.info("Perfil deportivo actualizado para jugador: " + sanitize(jugadorId));
        return actualizado;
    }

    @Override
    public PerfilDeportivo consultarPerfil(String jugadorId) {
        return perfilRepository.findByJugadorId(jugadorId)
                .orElseThrow(() -> new IllegalArgumentException("El jugador no tiene perfil deportivo registrado."));
    }
}
