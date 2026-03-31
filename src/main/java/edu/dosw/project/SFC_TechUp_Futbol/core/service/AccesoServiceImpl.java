package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RolFuncional;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.CapitanRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.AccesoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AccesoServiceImpl implements AccesoService {

    private static final Logger log = Logger.getLogger(AccesoServiceImpl.class.getName());

    private final UsuarioRegistradoRepository usuarioRepository;
    private final OrganizadorRepository organizadorRepository;
    private final ArbitroRepository arbitroRepository;
    private final CapitanRepository capitanRepository;
    private final JwtService jwtService;

    public AccesoServiceImpl(UsuarioRegistradoRepository usuarioRepository,
                             OrganizadorRepository organizadorRepository,
                             ArbitroRepository arbitroRepository,
                             CapitanRepository capitanRepository,
                             JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.organizadorRepository = organizadorRepository;
        this.arbitroRepository = arbitroRepository;
        this.capitanRepository = capitanRepository;
        this.jwtService = jwtService;
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
        String email = request.getEmail();
        String password = request.getPassword();

        var org = organizadorRepository.findByEmail(email);
        if (org.isPresent()) {
            if (!PasswordUtil.verificar(password, org.get().getPassword()))
                throw new IllegalArgumentException("Credenciales incorrectas.");
            log.info("Login organizador exitoso");
            return new LoginResponse(jwtService.generarToken(email, RolFuncional.ORGANIZADOR),
                    org.get().getName(), email, org.get().getUserType());
        }

        var arb = arbitroRepository.findByEmail(email);
        if (arb.isPresent()) {
            if (!PasswordUtil.verificar(password, arb.get().getPassword()))
                throw new IllegalArgumentException("Credenciales incorrectas.");
            log.info("Login arbitro exitoso");
            return new LoginResponse(jwtService.generarToken(email, RolFuncional.ARBITRO),
                    arb.get().getName(), email, arb.get().getUserType());
        }

        var cap = capitanRepository.findByEmail(email);
        if (cap.isPresent()) {
            if (!PasswordUtil.verificar(password, cap.get().getPassword()))
                throw new IllegalArgumentException("Credenciales incorrectas.");
            log.info("Login capitan exitoso");
            return new LoginResponse(jwtService.generarToken(email, RolFuncional.CAPITAN),
                    cap.get().getName(), email, cap.get().getUserType());
        }

        UsuarioRegistrado usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas."));
        if (!PasswordUtil.verificar(password, usuario.getPassword()))
            throw new IllegalArgumentException("Credenciales incorrectas.");
        log.info("Login jugador exitoso");
        return AccesoMapper.toLoginResponse(usuario, jwtService.generarToken(email, RolFuncional.JUGADOR));
    }
}
