package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.OAuth2Response;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.RolFuncional;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.UsuarioRegistradoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.UsuarioRegistradoJpaRepository;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class OAuth2Service {

    private static final Logger log = Logger.getLogger(OAuth2Service.class.getName());

    private static String sanitize(String input) {
        return input == null ? "null" : input.replaceAll("[\r\n\t]", "_");
    }

    private final UsuarioRegistradoJpaRepository usuarioRepository;
    private final UsuarioRegistradoMapper mapper;
    private final JwtService jwtService;

    public OAuth2Service(UsuarioRegistradoJpaRepository usuarioRepository, UsuarioRegistradoMapper mapper, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.jwtService = jwtService;
    }

    public OAuth2Response procesarCallback(OAuth2AuthenticationToken authentication) {
        try {
        Map<String, Object> atributos = authentication.getPrincipal().getAttributes();
        String email = (String) atributos.get("email");
        String nombre = (String) atributos.getOrDefault("name", email);

        if (email == null || !email.endsWith("@gmail.com")) {
            throw new IllegalArgumentException("El correo de Google debe ser un Gmail válido.");
        }

        UsuarioRegistrado usuario = usuarioRepository.findByEmail(email)
                .map(mapper::toDomain)
                .orElseGet(() -> {
                    log.info("Usuario OAuth2 nuevo, registrando: " + sanitize(email));
                    UsuarioRegistrado nuevo = new UsuarioRegistrado(
                            IdGeneratorUtil.generarId(), nombre, email,
                            PasswordUtil.cifrar(UUID.randomUUID().toString()),
                            Usuario.TipoUsuario.FAMILIAR
                    );
                    mapper.toDomain(usuarioRepository.save(mapper.toEntity(nuevo)));
                    return nuevo;
                });

        String token = jwtService.generarToken(usuario.getEmail(), RolFuncional.JUGADOR);
        log.info("Login OAuth2 exitoso: " + sanitize(email));
        return new OAuth2Response(token);
        } catch (Exception e) {
            log.severe("ERROR OAuth2: " + e.getClass().getName() + " - " + e.getMessage());
            throw e;
        }
    }
}
