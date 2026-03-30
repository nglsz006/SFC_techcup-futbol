package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void loginRequest_gettersSetters() {
        LoginRequest req = new LoginRequest();
        req.setEmail("juan@test.com");
        req.setPassword("pass123");
        assertEquals("juan@test.com", req.getEmail());
        assertEquals("pass123", req.getPassword());
    }

    @Test
    void registroRequest_gettersSetters() {
        RegistroRequest req = new RegistroRequest();
        req.setNombre("Juan");
        req.setEmail("juan@escuelaing.edu.co");
        req.setPassword("12345678");
        req.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        assertEquals("Juan", req.getNombre());
        assertEquals("juan@escuelaing.edu.co", req.getEmail());
        assertEquals("12345678", req.getPassword());
        assertEquals(Usuario.TipoUsuario.ESTUDIANTE, req.getTipoUsuario());
    }

    @Test
    void loginResponse_getters() {
        LoginResponse resp = new LoginResponse("tok123", "Ana", "ana@test.com", Usuario.TipoUsuario.ESTUDIANTE);
        assertEquals("tok123", resp.getToken());
        assertEquals("Ana", resp.getNombre());
        assertEquals("ana@test.com", resp.getEmail());
        assertEquals(Usuario.TipoUsuario.ESTUDIANTE, resp.getTipoUsuario());
    }

    @Test
    void usuarioResponse_getters() {
        UsuarioResponse resp = new UsuarioResponse("uuid-1", "Carlos", "carlos@test.com", Usuario.TipoUsuario.ESTUDIANTE);
        assertEquals("uuid-1", resp.getId());
        assertEquals("Carlos", resp.getNombre());
        assertEquals("carlos@test.com", resp.getEmail());
        assertEquals(Usuario.TipoUsuario.ESTUDIANTE, resp.getTipoUsuario());
    }
}
