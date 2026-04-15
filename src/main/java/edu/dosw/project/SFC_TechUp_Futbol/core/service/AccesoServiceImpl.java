package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.RolFuncional;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.AdministradorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.ArbitroMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.CapitanMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.OrganizadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.UsuarioRegistradoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AdministradorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.ArbitroJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.CapitanJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.OrganizadorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.UsuarioRegistradoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.AccesoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AccesoServiceImpl implements AccesoService {

    private static final Logger log = Logger.getLogger(AccesoServiceImpl.class.getName());

    private final UsuarioRegistradoJpaRepository usuarioRepository;
    private final UsuarioRegistradoMapper usuarioMapper;
    private final OrganizadorJpaRepository organizadorRepository;
    private final OrganizadorMapper organizadorMapper;
    private final ArbitroJpaRepository arbitroRepository;
    private final ArbitroMapper arbitroMapper;
    private final CapitanJpaRepository capitanRepository;
    private final CapitanMapper capitanMapper;
    private final AdministradorJpaRepository administradorRepository;
    private final AdministradorMapper administradorMapper;
    private final JwtService jwtService;

    public AccesoServiceImpl(UsuarioRegistradoJpaRepository usuarioRepository,
                             UsuarioRegistradoMapper usuarioMapper,
                             OrganizadorJpaRepository organizadorRepository,
                             OrganizadorMapper organizadorMapper,
                             ArbitroJpaRepository arbitroRepository,
                             ArbitroMapper arbitroMapper,
                             CapitanJpaRepository capitanRepository,
                             CapitanMapper capitanMapper,
                             AdministradorJpaRepository administradorRepository,
                             AdministradorMapper administradorMapper,
                             JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.organizadorRepository = organizadorRepository;
        this.organizadorMapper = organizadorMapper;
        this.arbitroRepository = arbitroRepository;
        this.arbitroMapper = arbitroMapper;
        this.capitanRepository = capitanRepository;
        this.capitanMapper = capitanMapper;
        this.administradorRepository = administradorRepository;
        this.administradorMapper = administradorMapper;
        this.jwtService = jwtService;
    }

    @Override
    public UsuarioResponse registrar(RegistroRequest request) {
        if (usuarioRepository.findByEmail(Base64Util.encode(request.getEmail())).isPresent())
            throw new IllegalStateException("Ya existe un usuario con ese correo.");
        UsuarioRegistrado usuario = AccesoMapper.toModelo(request);
        if (usuario.getId() == null) usuario.setId(edu.dosw.project.SFC_TechUp_Futbol.core.util.IdGeneratorUtil.generarId());
        usuarioRepository.save(usuarioMapper.toEntity(usuario));
        log.info("Usuario registrado exitosamente");
        return AccesoMapper.toUsuarioResponse(usuario);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String emailEncoded = Base64Util.encode(email);

        var adminEntity = administradorRepository.findByEmail(emailEncoded);
        if (adminEntity.isPresent()) {
            var admin = administradorMapper.toDomain(adminEntity.get());
            if (!PasswordUtil.verificar(password, admin.getPassword()))
                throw new IllegalArgumentException("Credenciales incorrectas.");
            log.info("Login administrador exitoso");
            return new LoginResponse(jwtService.generarToken(email, RolFuncional.ADMINISTRADOR),
                    admin.getName(), email, admin.getUserType());
        }

        var orgEntity = organizadorRepository.findByEmail(emailEncoded);
        if (orgEntity.isPresent()) {
            var org = organizadorMapper.toDomain(orgEntity.get());
            if (!PasswordUtil.verificar(password, org.getPassword()))
                throw new IllegalArgumentException("Credenciales incorrectas.");
            log.info("Login organizador exitoso");
            return new LoginResponse(jwtService.generarToken(email, RolFuncional.ORGANIZADOR),
                    org.getName(), email, org.getUserType());
        }

        var arbEntity = arbitroRepository.findByEmail(emailEncoded);
        if (arbEntity.isPresent()) {
            var arb = arbitroMapper.toDomain(arbEntity.get());
            if (!PasswordUtil.verificar(password, arb.getPassword()))
                throw new IllegalArgumentException("Credenciales incorrectas.");
            log.info("Login arbitro exitoso");
            return new LoginResponse(jwtService.generarToken(email, RolFuncional.ARBITRO),
                    arb.getName(), email, arb.getUserType());
        }

        var capEntity = capitanRepository.findByEmail(emailEncoded);
        if (capEntity.isPresent()) {
            var cap = capitanMapper.toDomain(capEntity.get());
            if (!PasswordUtil.verificar(password, cap.getPassword()))
                throw new IllegalArgumentException("Credenciales incorrectas.");
            log.info("Login capitan exitoso");
            return new LoginResponse(jwtService.generarToken(email, RolFuncional.CAPITAN),
                    cap.getName(), email, cap.getUserType());
        }

        UsuarioRegistrado usuario = usuarioRepository.findByEmail(emailEncoded)
                .map(usuarioMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas."));
        if (!PasswordUtil.verificar(password, usuario.getPassword()))
            throw new IllegalArgumentException("Credenciales incorrectas.");
        log.info("Login jugador exitoso");
        return AccesoMapper.toLoginResponse(usuario, jwtService.generarToken(email, RolFuncional.JUGADOR));
    }
}
