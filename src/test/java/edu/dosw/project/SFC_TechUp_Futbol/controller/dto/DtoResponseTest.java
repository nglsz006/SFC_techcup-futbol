package edu.dosw.project.SFC_TechUp_Futbol.controller.dto;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoResponseTest {

    @Test
    void registroAuditoriaResponse_getters() {
        LocalDateTime fecha = LocalDateTime.of(2025, 1, 1, 10, 0);
        RegistroAuditoriaResponse r = new RegistroAuditoriaResponse("1", "adminId", "user@test.com", "REGISTRO_ORGANIZADOR", "desc", fecha);
        assertEquals("1", r.getId());
        assertEquals("adminId", r.getAdministradorId());
        assertEquals("user@test.com", r.getUsuario());
        assertEquals("REGISTRO_ORGANIZADOR", r.getTipoAccion());
        assertEquals("desc", r.getDescripcion());
        assertEquals(fecha, r.getFecha());
    }

    @Test
    void registroAdministrativoResponse_getters() {
        RegistroAdministrativoResponse r = new RegistroAdministrativoResponse("1", "Juan", "juan@test.com", Usuario.TipoUsuario.ESTUDIANTE, "ORGANIZADOR", "adminId");
        assertEquals("1", r.getId());
        assertEquals("Juan", r.getNombre());
        assertEquals("juan@test.com", r.getEmail());
        assertEquals(Usuario.TipoUsuario.ESTUDIANTE, r.getTipoUsuario());
        assertEquals("ORGANIZADOR", r.getRol());
        assertEquals("adminId", r.getRegistradoPor());
    }

    @Test
    void administradorLoginResponse_getters() {
        AdministradorLoginResponse r = new AdministradorLoginResponse("adminId", "Admin", "admin@test.com", "token123");
        assertEquals("adminId", r.getAdministradorId());
        assertEquals("Admin", r.getNombre());
        assertEquals("admin@test.com", r.getEmail());
        assertEquals("token123", r.getToken());
    }

    @Test
    void consultaAuditoriaResponse_getters() {
        RegistroAuditoriaResponse reg = new RegistroAuditoriaResponse("1", "a", "u", "TIPO", "d", LocalDateTime.now());
        ConsultaAuditoriaResponse r = new ConsultaAuditoriaResponse("ok", List.of(reg));
        assertEquals("ok", r.getMensaje());
        assertEquals(1, r.getRegistros().size());
    }
}
