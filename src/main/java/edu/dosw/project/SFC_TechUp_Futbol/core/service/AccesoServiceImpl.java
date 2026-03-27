package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.AccesoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
public class AccesoServiceImpl implements AccesoService {

    private static final Logger log = Logger.getLogger(AccesoServiceImpl.class.getName());

    private final UsuarioRegistradoRepository usuarioRepository;

    public AccesoServiceImpl(UsuarioRegistradoRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioResponse registrar(RegistroRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent())
            throw new IllegalStateException("Ya existe un usuario con ese correo.");

        UsuarioRegistrado usuario = AccesoMapper.toModelo(request);
        usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente");
        return AccesoMapper.toUsuarioResponse(usuario);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UsuarioRegistrado usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas."));

        if (!PasswordUtil.verificar(request.getPassword(), usuario.getPassword()))
            throw new IllegalArgumentException("Credenciales incorrectas.");

        String token = UUID.randomUUID().toString();
        log.info("Login exitoso");
        return AccesoMapper.toLoginResponse(usuario, token);
    }
}
