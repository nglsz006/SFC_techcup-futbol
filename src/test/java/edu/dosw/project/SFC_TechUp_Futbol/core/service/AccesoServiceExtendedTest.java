package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccesoServiceExtendedTest {

    private AccesoServiceImpl service;
    private UsuarioRegistradoJpaRepository usuarioRepo;
    private OrganizadorJpaRepository orgRepo;
    private ArbitroJpaRepository arbRepo;
    private CapitanJpaRepository capRepo;

    @BeforeEach
    void setUp() {
        UsuarioRegistradoMapper usuarioMapper = TestMappers.usuarioRegistradoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        EquipoMapper equipoMapper = TestMappers.equipoMapper();

        Map<String, UsuarioRegistradoEntity> usuarioStore = new HashMap<>();
        usuarioRepo = mock(UsuarioRegistradoJpaRepository.class);
        when(usuarioRepo.save(any())).thenAnswer(inv -> {
            UsuarioRegistradoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            usuarioStore.put(e.getId(), e);
            return e;
        });
        when(usuarioRepo.findByEmail(anyString())).thenAnswer(inv ->
                usuarioStore.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        orgRepo = mock(OrganizadorJpaRepository.class);
        when(orgRepo.save(any())).thenAnswer(inv -> {
            OrganizadorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            orgStore.put(e.getId(), e);
            return e;
        });
        when(orgRepo.findByEmail(anyString())).thenAnswer(inv ->
                orgStore.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());

        Map<String, ArbitroEntity> arbStore = new HashMap<>();
        arbRepo = mock(ArbitroJpaRepository.class);
        when(arbRepo.findByEmail(anyString())).thenAnswer(inv ->
                arbStore.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());

        Map<String, CapitanEntity> capStore = new HashMap<>();
        capRepo = mock(CapitanJpaRepository.class);
        when(capRepo.save(any())).thenAnswer(inv -> {
            CapitanEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            capStore.put(e.getId(), e);
            return e;
        });
        when(capRepo.findByEmail(anyString())).thenAnswer(inv ->
                capStore.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());

        TorneoJpaRepository torneoRepo = mock(TorneoJpaRepository.class);
        when(torneoRepo.findById(anyString())).thenReturn(Optional.empty());
        OrganizadorMapper orgMapper = TestMappers.organizadorMapper(torneoRepo, TestMappers.torneoMapper());
        ArbitroMapper arbMapper = TestMappers.arbitroMapper(partidoMapper);
        CapitanMapper capMapper = TestMappers.capitanMapper(equipoRepo(), equipoMapper);

        service = new AccesoServiceImpl(usuarioRepo, usuarioMapper, orgRepo, orgMapper, arbRepo, arbMapper, capRepo, capMapper, new JwtService());
    }

    private EquipoJpaRepository equipoRepo() {
        EquipoJpaRepository r = mock(EquipoJpaRepository.class);
        when(r.findById(anyString())).thenReturn(Optional.empty());
        return r;
    }

    private RegistroRequest registroRequest(String email) {
        RegistroRequest req = new RegistroRequest();
        req.setNombre("Juan");
        req.setEmail(email);
        req.setPassword("12345678");
        req.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        req.setCarrera(Usuario.Carrera.INGENIERIA_SISTEMAS);
        return req;
    }

    @Test
    void registrar_nuevo_retornaUsuario() {
        var resp = service.registrar(registroRequest("juan@escuelaing.edu.co"));
        assertNotNull(resp.getId());
        assertEquals("juan@escuelaing.edu.co", resp.getEmail());
    }

    @Test
    void registrar_correoRepetido_lanzaExcepcion() {
        service.registrar(registroRequest("juan@escuelaing.edu.co"));
        assertThrows(IllegalStateException.class, () -> service.registrar(registroRequest("juan@escuelaing.edu.co")));
    }

    @Test
    void login_usuarioRegistrado_retornaToken() {
        service.registrar(registroRequest("juan@escuelaing.edu.co"));
        LoginRequest req = new LoginRequest();
        req.setEmail("juan@escuelaing.edu.co");
        req.setPassword("12345678");
        var resp = service.login(req);
        assertNotNull(resp.getToken());
    }

    @Test
    void login_passwordIncorrecta_lanzaExcepcion() {
        service.registrar(registroRequest("juan@escuelaing.edu.co"));
        LoginRequest req = new LoginRequest();
        req.setEmail("juan@escuelaing.edu.co");
        req.setPassword("wrongpass");
        assertThrows(IllegalArgumentException.class, () -> service.login(req));
    }

    @Test
    void login_usuarioInexistente_lanzaExcepcion() {
        LoginRequest req = new LoginRequest();
        req.setEmail("noexiste@escuelaing.edu.co");
        req.setPassword("12345678");
        assertThrows(IllegalArgumentException.class, () -> service.login(req));
    }
}
