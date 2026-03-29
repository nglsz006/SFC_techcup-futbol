package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepository;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AccesoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AccesoServiceImpl;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccesoServiceTest {

    private AccesoService accesoService;

    @BeforeEach
    void setUp() {
        Map<String, UsuarioRegistrado> store = new HashMap<>();
        AtomicLong idGen = new AtomicLong(1);
        UsuarioRegistradoRepository repo = mock(UsuarioRegistradoRepository.class);
        when(repo.save(any())).thenAnswer(inv -> {
            UsuarioRegistrado u = inv.getArgument(0);
            if (u.getId() == null) u.setId(java.util.UUID.randomUUID().toString());
            store.put(u.getId(), u);
            return u;
        });
        when(repo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return store.values().stream().filter(u -> email.equals(u.getEmail())).findFirst();
        });
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        accesoService = new AccesoServiceImpl(repo, new JwtService());
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
        assertThrows(IllegalStateException.class, () -> accesoService.registrar(registroValido()));
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
        assertThrows(IllegalArgumentException.class, () -> accesoService.login(req));
    }

    @Test
    void login_emailNoExiste_lanzaExcepcion() {
        LoginRequest req = new LoginRequest();
        req.setEmail("noexiste@escuelaing.edu.co");
        req.setPassword("12345678");
        assertThrows(IllegalArgumentException.class, () -> accesoService.login(req));
    }
}
