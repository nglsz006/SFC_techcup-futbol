package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.AccesoMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccesoMapperTest {

    private RegistroRequest crearRequest() {
        RegistroRequest req = new RegistroRequest();
        req.setNombre("Juan");
        req.setEmail("juan@escuelaing.edu.co");
        req.setPassword("12345678");
        req.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        return req;
    }

    @Test
    void toModelo_mapea_correctamente() {
        UsuarioRegistrado usuario = AccesoMapper.toModelo(crearRequest());
        assertEquals("Juan", usuario.getName());
        assertEquals("juan@escuelaing.edu.co", usuario.getEmail());
        assertEquals(Usuario.TipoUsuario.ESTUDIANTE, usuario.getUserType());
        assertNotNull(usuario.getPassword());
    }

    @Test
    void toModelo_cifra_password() {
        UsuarioRegistrado usuario = AccesoMapper.toModelo(crearRequest());
        assertNotEquals("12345678", usuario.getPassword());
    }

    @Test
    void toUsuarioResponse_mapea_correctamente() {
        UsuarioRegistrado usuario = new UsuarioRegistrado("uuid-1", "Juan", "juan@escuelaing.edu.co", "hash", Usuario.TipoUsuario.ESTUDIANTE);
        UsuarioResponse response = AccesoMapper.toUsuarioResponse(usuario);
        assertEquals("uuid-1", response.getId());
        assertEquals("Juan", response.getNombre());
        assertEquals("juan@escuelaing.edu.co", response.getEmail());
        assertEquals(Usuario.TipoUsuario.ESTUDIANTE, response.getTipoUsuario());
    }

    @Test
    void toLoginResponse_mapea_correctamente() {
        UsuarioRegistrado usuario = new UsuarioRegistrado("uuid-1", "Juan", "juan@escuelaing.edu.co", "hash", Usuario.TipoUsuario.ESTUDIANTE);
        LoginResponse response = AccesoMapper.toLoginResponse(usuario, "token123");
        assertEquals("token123", response.getToken());
        assertEquals("Juan", response.getNombre());
        assertEquals(Usuario.TipoUsuario.ESTUDIANTE, response.getTipoUsuario());
    }
}
