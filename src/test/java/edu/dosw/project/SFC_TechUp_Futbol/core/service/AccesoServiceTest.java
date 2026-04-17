package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AutenticacionAdminException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AccesoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AccesoServiceImpl;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.UsuarioRegistradoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.OrganizadorEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.ArbitroEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.CapitanEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.UsuarioRegistradoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.OrganizadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.ArbitroMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.CapitanMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.PartidoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.TorneoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.JugadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.UsuarioRegistradoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.OrganizadorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.ArbitroJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.CapitanJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.TorneoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.EquipoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccesoServiceTest {

    private AccesoService accesoService;

    @BeforeEach
    void setUp() {
        Map<String, edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity> jugadorStore = new HashMap<>();
        
        UsuarioRegistradoJpaRepository repo = mock(UsuarioRegistradoJpaRepository.class);
        when(repo.existsEmailEnTablaUsuario(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return jugadorStore.values().stream().anyMatch(e -> email.equals(e.getEmail()));
        });
        when(repo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return jugadorStore.values().stream()
                .filter(e -> email.equals(e.getEmail()))
                .map(j -> {
                    UsuarioRegistradoEntity u = new UsuarioRegistradoEntity();
                    u.setId(j.getId());
                    u.setName(j.getName());
                    u.setEmail(j.getEmail());
                    u.setPassword(j.getPassword());
                    u.setUserType(j.getUserType());
                    return u;
                })
                .findFirst();
        });

        OrganizadorJpaRepository orgRepo = mock(OrganizadorJpaRepository.class);
        when(orgRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        ArbitroJpaRepository arbRepo = mock(ArbitroJpaRepository.class);
        when(arbRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        CapitanJpaRepository capRepo = mock(CapitanJpaRepository.class);
        when(capRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AdministradorJpaRepository adminRepo = mock(edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AdministradorJpaRepository.class);
        when(adminRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        TorneoJpaRepository torneoRepo = mock(TorneoJpaRepository.class);
        EquipoJpaRepository equipoRepo = mock(EquipoJpaRepository.class);

        UsuarioRegistradoMapper usuarioMapper = TestMappers.usuarioRegistradoMapper();
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        OrganizadorMapper orgMapper = TestMappers.organizadorMapper(torneoRepo, torneoMapper);
        ArbitroMapper arbMapper = TestMappers.arbitroMapper(partidoMapper);
        CapitanMapper capMapper = TestMappers.capitanMapper(equipoRepo, equipoMapper);
        edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.AdministradorMapper adminMapper = TestMappers.administradorMapper();

        edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository jugadorRepo =
                mock(edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.JugadorJpaRepository.class);
        when(jugadorRepo.save(any())).thenAnswer(inv -> {
            edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            jugadorStore.put(e.getId(), e);
            return e;
        });
        when(jugadorRepo.findByEmail(anyString())).thenAnswer(inv ->
                jugadorStore.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());
        when(jugadorRepo.findById(anyString())).thenAnswer(inv ->
                Optional.ofNullable(jugadorStore.get(inv.<String>getArgument(0))));
        when(jugadorRepo.findAll()).thenAnswer(inv -> new ArrayList<>(jugadorStore.values()));

        accesoService = new AccesoServiceImpl(repo, usuarioMapper, orgRepo, orgMapper, arbRepo, arbMapper, capRepo, capMapper, adminRepo, adminMapper, new JwtService(),
                jugadorRepo, jugadorMapper);
    }

    private RegistroRequest registroValido() {
        RegistroRequest req = new RegistroRequest();
        req.setNombre("Juan");
        req.setEmail("juan@escuelaing.edu.co");
        req.setPassword("12345678");
        req.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        return req;
    }

    @Test
    void registrar_usuarioNuevo_retornaResponse() {
        UsuarioResponse response = accesoService.registrar(registroValido());
        assertNotNull(response.getId());
        assertEquals("juan@escuelaing.edu.co", response.getEmail());
    }

    @Test
    void registrar_correoRepetido_lanzaExcepcion() {
        accesoService.registrar(registroValido());
        assertThrows(CorreoYaRegistradoException.class, () -> accesoService.registrar(registroValido()));
    }

    @Test
    void login_credencialesCorrectas_retornaToken() {
        accesoService.registrar(registroValido());
        LoginRequest req = new LoginRequest();
        req.setEmail("juan@escuelaing.edu.co");
        req.setPassword("12345678");
        LoginResponse response = accesoService.login(req);
        assertNotNull(response.getToken());
        assertEquals("Juan", response.getNombre());
    }

    @Test
    void login_passwordIncorrecta_lanzaExcepcion() {
        accesoService.registrar(registroValido());
        LoginRequest req = new LoginRequest();
        req.setEmail("juan@escuelaing.edu.co");
        req.setPassword("wrongpassword");
        assertThrows(AutenticacionAdminException.class, () -> accesoService.login(req));
    }

    @Test
    void login_emailNoExiste_lanzaExcepcion() {
        LoginRequest req = new LoginRequest();
        req.setEmail("noexiste@escuelaing.edu.co");
        req.setPassword("12345678");
        assertThrows(AutenticacionAdminException.class, () -> accesoService.login(req));
    }
}
