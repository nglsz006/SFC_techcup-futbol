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
    public PerfilDeportivo crearPerfil(Long jugadorId, List<Jugador.Posicion> posiciones, int dorsal,
                                       String foto, int edad, PerfilDeportivo.Genero genero,
                                       String identificacion, Integer semestre) {
        jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado con id: " + jugadorId));

        if (perfilRepository.findByJugadorId(jugadorId).isPresent()) {
            throw new IllegalStateException("El jugador ya tiene un perfil deportivo creado.");
        }

        validator.validarPerfil(posiciones, dorsal, edad, genero, identificacion);
        validator.validarSemestre(semestre);

        PerfilDeportivo perfil = new PerfilDeportivo(null, jugadorId, posiciones, dorsal,
                foto, edad, genero, identificacion, semestre);

        PerfilDeportivo guardado = perfilRepository.save(perfil);
        log.info("Perfil deportivo creado para jugador: " + jugadorId);
        return guardado;
    }

    @Override
    public PerfilDeportivo editarPerfil(Long jugadorId, List<Jugador.Posicion> posiciones, int dorsal,
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
        log.info("Perfil deportivo actualizado para jugador: " + jugadorId);
        return actualizado;
    }

    @Override
    public PerfilDeportivo consultarPerfil(Long jugadorId) {
        return perfilRepository.findByJugadorId(jugadorId)
                .orElseThrow(() -> new IllegalArgumentException("El jugador no tiene perfil deportivo registrado."));
    }
}
